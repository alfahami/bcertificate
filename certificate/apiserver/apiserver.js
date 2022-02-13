var express = require('express');
var bodyParser = require('body-parser');
var cors = require("cors");
var urlencodedParser = bodyParser.urlencoded({extended: true})

var multer = require('multer');
var xlstojson = require("xls-to-json-lc");
var xlsxtojson = require("xlsx-to-json-lc");
var app = express();
app.use(cors());
app.use(bodyParser.json());

var xlsxtojson = require("xlsx-to-json");
var xlstojson = require("xls-to-json");


// Setting for Hyperledger Fabric
const { Gateway,Wallets } = require('fabric-network');
const path = require('path');
const fs = require('fs');
//const ccpPath = path.resolve(__dirname, '..', '..', 'test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
  //      const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));
  app.set("view engine","pug");

  app.get('/api/', function (req, res) {
  
      res.render('createcertificate');
  
  });

  app.get('/api/addcertificate', function (req, res) {
        res.render('createcertificate');
  });

  app.get('/api/', function (req, res) {
        res.render('createcertificate');
  });



app.get('/api/allcertificates', async function (req, res)  {
    try {
const ccpPath = path.resolve(__dirname, '..', '..', 'test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
        const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));
// Create a new file system based wallet for managing identities.
        const walletPath = path.join(process.cwd(), 'wallet');
        const wallet = await Wallets.newFileSystemWallet(walletPath);
        console.log(`Wallet path: ${walletPath}`);

        // Check to see if we've already enrolled the user.
        const identity = await wallet.get('appUser');
        if (!identity) {
            console.log('An identity for the user "appUser" does not exist in the wallet');
            console.log('Run the registerUser.js application before retrying');
            return;
        }
  // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();
        await gateway.connect(ccp, { wallet, identity: 'appUser', discovery: { enabled: true, asLocalhost: true } });

        // Get the network (channel) our contract is deployed to.
        const network = await gateway.getNetwork('mychannel');

        // Get the contract from the network.
        const contract = network.getContract('certificate');

        // Evaluate the specified transaction.
        // queryCar transaction - requires 1 argument, ex: ('queryCertificate', 'CERT4')
        // queryAllCars transaction - requires no arguments, ex: ('queryAllCars')
        const result = await contract.evaluateTransaction('queryAllCertificates');
	console.log(JSON.parse(result)[0]["Record"]);
        console.log(`Transaction has been evaluated, result is: ${result.toString()}`);
        res.render("allcertificates",{ list:JSON.parse(result)});
} catch (error) {
        console.error(`Failed to evaluate transaction: ${error}`);
        res.status(500).json({error: error});
        process.exit(1);
    }
});


/* code for uploading excel data*/

var storage = multer.diskStorage({ //multers disk storage settings
        destination: function (req, file, cb) {
            cb(null, './uploads/')
        },
        filename: function (req, file, cb) {
            var datetimestamp = Date.now();
            cb(null, file.fieldname + '-' + datetimestamp + '.' + file.originalname.split('.')[file.originalname.split('.').length -1])
        }
    });
    var upload = multer({ //multer settings
                    storage: storage,
                    fileFilter : function(req, file, callback) { //file filter
                        if (['xls', 'xlsx'].indexOf(file.originalname.split('.')[file.originalname.split('.').length-1]) === -1) {
                            return callback(new Error('Wrong extension type'));
                        }
                        callback(null, true);
                    }
                }).single('file');
                
    /** API path that will upload the files */
    app.post('/api/preview_excel_data', function(req, res) {
        var exceltojson;
        upload(req,res,function(err){
            if(err){
                 res.json({error_code:1,err_desc:err});
  
                 return;
            }
            /** Multer gives us file info in req.file object */
            if(!req.file){
                res.json({error_code:1,err_desc:"No file passed"});
                return;
            }
            /** Check the extension of the incoming file and
             *  use the appropriate module
             */
            if(req.file.originalname.split('.')[req.file.originalname.split('.').length-1] === 'xlsx'){
                exceltojson = xlsxtojson;
            } else {
                exceltojson = xlstojson;
            }
            try {
                exceltojson({
                    input: req.file.path,
                    output: null, //since we don't need output.json
                    lowerCaseHeaders:false
                }, async function(err,result){
                    if(err) {
                        return res.json({error_code:1,err_desc:err, data: null});
                    }
                    
                    for (const item of result) {
                        try {
                                const ccpPath = path.resolve(__dirname, '..', '..', 'test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
                                        const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));
                                // Create a new file system based wallet for managing identities.
                                        const walletPath = path.join(process.cwd(), 'wallet');
                                        const wallet = await Wallets.newFileSystemWallet(walletPath);
                                        console.log(`Wallet path: ${walletPath}`);
                                
                                        // Check to see if we've already enrolled the user.
                                        const identity = await wallet.get('appUser');
                                        if (!identity) {
                                            console.log('An identity for the user "appUser" does not exist in the wallet');
                                            console.log('Run the registerUser.js application before retrying');
                                            return;
                                        }
                                  // Create a new gateway for connecting to our peer node.
                                        const gateway = new Gateway();
                                        await gateway.connect(ccp, { wallet, identity: 'appUser', discovery: { enabled: true, asLocalhost: true } });
                                
                                        // Get the network (channel) our contract is deployed to.
                                        const network = await gateway.getNetwork('mychannel');
                                
                                        // Get the contract from the network.
                                        const contract = network.getContract('certificate');
                                // Submit the specified transaction.
                                        // createCertificate transaction - requires 8 argument, ex: ('createCertificate', 'CERT12', 'Honda', 'Accord', 'Black', 'Tom')
                                        // changeCarOwner transaction - requires 2 args , ex: ('changeStudentName', 'CERT10', 'HADI')
                                        await contract.submitTransaction('createCertificate', item["certificateNumber"], item["studentFullName"], item["apogee"], item["cin"], item["birthDate"], item["birthPlace"], 
                                        item["title"], item["honor"], item["graduationDate"]);
                                        console.log('Transaction has been submitted');
                                       // res.redirect('/api/query/' + req.body.certificateNumber)
                                       
                                
                                // Disconnect from the gateway.
                                        await gateway.disconnect();
                                } catch (error) {
                                        console.error(`Failed to submit transaction: ${error}`);
                                        process.exit(1);
                                }    
                    }

                });
            } catch (e){
                res.json({error_code:1,err_desc:"Corupted excel file"});
            }
        })
    });



app.post('/api/addcertificate', urlencodedParser, async function (req, res) {
    try {
const ccpPath = path.resolve(__dirname, '..', '..', 'test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
        const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));
// Create a new file system based wallet for managing identities.
        const walletPath = path.join(process.cwd(), 'wallet');
        const wallet = await Wallets.newFileSystemWallet(walletPath);
        console.log(`Wallet path: ${walletPath}`);

        // Check to see if we've already enrolled the user.
        const identity = await wallet.get('appUser');
        if (!identity) {
            console.log('An identity for the user "appUser" does not exist in the wallet');
            console.log('Run the registerUser.js application before retrying');
            return;
        }
  // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();
        await gateway.connect(ccp, { wallet, identity: 'appUser', discovery: { enabled: true, asLocalhost: true } });

        // Get the network (channel) our contract is deployed to.
        const network = await gateway.getNetwork('mychannel');

        // Get the contract from the network.
        const contract = network.getContract('certificate');
// Submit the specified transaction.
        // createCertificate transaction - requires 8 argument, ex: ('createCertificate', 'CERT12', 'Honda', 'Accord', 'Black', 'Tom')
        // changeCarOwner transaction - requires 2 args , ex: ('changeStudentName', 'CERT10', 'HADI')
        await contract.submitTransaction('createCertificate', req.body.certificateNumber, req.body.studentFullName, req.body.apogee, req.body.cin, req.body.birthDate, req.body.birthPlace, 
        req.body.title, req.body.honor, req.body.graduationDate);
        console.log('Transaction has been submitted');
        res.redirect('/api/query/' + req.body.certificateNumber)

// Disconnect from the gateway.
        await gateway.disconnect();
} catch (error) {
        console.error(`Failed to submit transaction: ${error}`);
        process.exit(1);
    }
})

app.get('/api/query/:certificate_index', async function (req, res) {
        try {
    const ccpPath = path.resolve(__dirname, '..', '..', 'test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
            const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));
    // Create a new file system based wallet for managing identities.
            const walletPath = path.join(process.cwd(), 'wallet');
            const wallet = await Wallets.newFileSystemWallet(walletPath);
            console.log(`Wallet path: ${walletPath}`);
    
            // Check to see if we've already enrolled the user.
            const identity = await wallet.get('appUser');
            if (!identity) {
                console.log('An identity for the user "appUser" does not exist in the wallet');
                console.log('Run the registerUser.js application before retrying');
                return;
            }
      // Create a new gateway for connecting to our peer node.
            const gateway = new Gateway();
            await gateway.connect(ccp, { wallet, identity: 'appUser', discovery: { enabled: true, asLocalhost: true } });
    
            // Get the network (channel) our contract is deployed to.
            const network = await gateway.getNetwork('mychannel');
    
            // Get the contract from the network.
            const contract = network.getContract('certificate');
    // Evaluate the specified transaction.
            // queryCar transaction - requires 1 argument, ex: ('queryCar', 'CAR4')
            // queryAllCars transaction - requires no arguments, ex: ('queryAllCars')
            const result = await contract.evaluateTransaction('queryCertificate', req.params.certificate_index);
            console.log(`Transaction has been evaluated, result is: ${result.toString()}`);
            res.render("certificate-details",{ certificate:JSON.parse(result)});
    } catch (error) {
            console.error(`Failed to evaluate transaction: ${error}`);
            res.status(500).json({error: error});
            process.exit(1);
        }
    });
    


app.put('/api/changestudentname/:cert_index', async function (req, res) {
    try {
const ccpPath = path.resolve(__dirname, '..', '..', 'test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
        const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));
// Create a new file system based wallet for managing identities.
        const walletPath = path.join(process.cwd(), 'wallet');
        const wallet = await Wallets.newFileSystemWallet(walletPath);
        console.log(`Wallet path: ${walletPath}`);

        // Check to see if we've already enrolled the user.
        const identity = await wallet.get('appUser');
        if (!identity) {
            console.log('An identity for the user "appUser" does not exist in the wallet');
            console.log('Run the registerUser.js application before retrying');
            return;
        }
  // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();
        await gateway.connect(ccp, { wallet, identity: 'appUser', discovery: { enabled: true, asLocalhost: true } });

        // Get the network (channel) our contract is deployed to.
        const network = await gateway.getNetwork('mychannel'); 

        // Get the contract from the network.
        const contract = network.getContract('certificate');
// Submit the specified transaction.
        // createCar transaction - requires 5 argument, ex: ('createCar', 'CAR12', 'Honda', 'Accord', 'Black', 'Tom')
        // changeCarOwner transaction - requires 2 args , ex: ('changeCarOwner', 'CAR10', 'Dave')
        await contract.submitTransaction('changeStudentName', req.params.cert_index, req.body.studentFullName);
        console.log('Transaction has been submitted');
        res.send('Transaction has been submitted');
// Disconnect from the gateway.
        await gateway.disconnect();
} catch (error) {
        console.error(`Failed to submit transaction: ${error}`);
        process.exit(1);
    } 
})

app.listen(8080);

