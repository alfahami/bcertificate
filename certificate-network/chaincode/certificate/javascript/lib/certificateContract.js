/*
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Contract } = require('fabric-contract-api');

class CertificateContract extends Contract {

    async initLedger(ctx) {
        console.info('============= START : Initialize Ledger ===========');
        const certificates = [
            {
                studentFullName: 'ALFAHAMI TOIHIR',
                apogee: '18013942',
                cin: 'NBE388507',
                birthDate: '21/05/1992',
                birthPlace: 'MORONI',
                title: 'MASTER GLCL',
                honor: 'Assez-bien',
                graduationDate: '07/12/2021',

            },
            {
                studentFullName: 'MOULAY YOUSSEF HADI',
                apogee: '01313940',
                cin: 'MAR388514',
                birthDate: '15/05/1980',
                birthPlace: 'RABAT',
                title: 'PR. ES SCIENCE',
                honor: 'Honorable',
                graduationDate: '07/12/2021',
            },
            {
                studentFullName: 'BEHRAM MAHAMAT',
                apogee: '15874963',
                cin: 'TCH345583',
                birthDate: '11/03/1996',
                birthPlace: 'NDJAMENA',
                title: 'MASTER GLCL',
                honor: 'Assez-bien',
                graduationDate: '07/12/2021',
            },
            {
                studentFullName: 'KHALI LAABOUDI',
                apogee: '15648792',
                cin: 'MAR3745603',
                birthDate: '14/05/1994',
                birthPlace: 'KENITRA',
                title: 'MASTER GLCL',
                honor: 'bien',
                graduationDate: '07/12/2021',
            },
            {
                studentFullName: 'KHADIJA EL FELLAH',
                apogee: '13254879',
                cin: 'MAR348501',
                birthDate: '11/01/1998',
                birthPlace: 'RABAT',
                title: 'MASTER GLCL',
                honor: 'Assez-bien',
                graduationDate: '07/12/2021',
            },
            {
                studentFullName: 'KHALID HOUSNI',
                apogee: '18025479',
                cin: 'MAR312536',
                birthDate: '13/02/1979',
                birthPlace: 'MORONI',
                title: 'DOCTEUR INFORMATIQUE',
                honor: 'Assez-bien',
                graduationDate: '07/12/2021',
            },
            {
                studentFullName: 'KELLY MANSARE',
                apogee: '14013965',
                cin: 'GUI347500',
                birthDate: '05/08/1995',
                birthPlace: 'MORONI',
                title: 'MASTER GLCL',
                honor: 'Assez-bien',
                graduationDate: '07/12/2021',
            },
            {
                studentFullName: 'SIDI BEN ARFA',
                apogee: '110245879',
                cin: 'NBE784569',
                birthDate: '03/02/1998',
                birthPlace: 'MORONI',
                title: 'MASTER GLCL',
                honor: 'Bien',
                graduationDate: '07/12/2021',
            },
            {
                studentFullName: 'SENBLI YOUNES',
                apogee: '14503698',
                cin: 'MAR388468',
                birthDate: '21/05/1992',
                birthPlace: 'CASABLANCA',
                title: 'MASTER GLCL',
                honor: 'Assez-bien',
                graduationDate: '07/12/2021'
            },
            {
                studentFullName: 'MONTACER MEHDI',
                apogee: '1801549',
                cin: 'MAR388436',
                birthDate: '11/05/1989',
                birthPlace: 'KENITRA',
                title: 'MASTER GLCL',
                honor: 'Assez-bien',
                graduationDate: '07/12/2021'
            },
        ];

        for (let i = 0; i < certificates.length; i++) {
            certificates[i].docType = 'certificate';
            await ctx.stub.putState('CERT' + i, Buffer.from(JSON.stringify(certificates[i])));
            console.info('Added <--> ', certificates[i]);
        }
        console.info('============= END : Initialize Ledger ===========');
    }

    async queryCertificate(ctx, certificateNumber) {
        const certificateAsBytes = await ctx.stub.getState(certificateNumber); // get the certificate from chaincode state
        if (!certificateAsBytes || certificateAsBytes.length === 0) {
            throw new Error(`${certificateNumber} does not exist`);
        }
        console.log(certificateAsBytes.toString());
        return certificateAsBytes.toString();
    }

    async createCertificate(ctx, certificateNumber, studentFullName, apogee, cin, birthDate, birthPlace, title, honor, graduationDate) {
        console.info('============= START : Create Certificate ===========');

        const certificate = {
            studentFullName,
            docType: 'certificate',
            apogee,
            cin,
            birthDate,
            birthPlace,
            title,
            honor,
            graduationDate
        };

        await ctx.stub.putState(certificateNumber, Buffer.from(JSON.stringify(certificate)));
        console.info('============= END : Create Certificate ===========');
    }

    async queryAllCertificates(ctx) {
        const startKey = '';
        const endKey = '';
        const allResults = [];
        for await (const {key, value} of ctx.stub.getStateByRange(startKey, endKey)) {
            const strValue = Buffer.from(value).toString('utf8');
            let record;
            try {
                record = JSON.parse(strValue);
            } catch (err) {
                console.log(err);
                record = strValue;
            }
            allResults.push({ Key: key, Record: record });
        }
        console.info(allResults);
        return JSON.stringify(allResults);
    }

    async changeStudentName(ctx, certificateNumber, newName) {
        console.info('============= START : changeStudentName ===========');

        const certificateAsBytes = await ctx.stub.getState(certificateNumber); // get the certificate from chaincode state
        if (!certificateAsBytes || certificateAsBytes.length === 0) {
            throw new Error(`${certificateNumber} does not exist`);
        }
        const certificate = JSON.parse(certificateAsBytes.toString());
        certificate.studentFullName = newName;

        await ctx.stub.putState(certificateNumber, Buffer.from(JSON.stringify(certificate)));
        console.info('============= END : changeStudentName ===========');
    }

}

module.exports = CertificateContract;
