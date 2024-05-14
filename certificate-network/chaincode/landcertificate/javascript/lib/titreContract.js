/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Contract } = require('fabric-contract-api');

class TitreContract extends Contract {

    async initLedger(ctx) {
        console.info('============= START : Initialize Ledger ===========');
        const titres = [
            {
                
                fullName: 'Zineb Ghayyati',
                cin: 'MA2150124K',
                address: '132 Rue 2 Hay Maamora Kenitra',
                email: 'zineb.ghayyaty@gmail.com ',
                indice: '41',
                city_fonc: 'Kenitra',
                special_indice: 'BIS',
                
            },
            {
                
                fullName: 'Mehdi Kaghat',
                cin: 'MA215457JK',
                address: '132 Rue 2 Hay Saknia Kenitra',
                email: 'mehdi.ghayyaty@gmail.com ',
                indice: '45',
                city_fonc: 'Kenitra',
                special_indice: 'BIS1',
                
            },
            {
                
                fullName: 'Ilyass Mohammed',
                cin: 'MA214788M',
                address: '8 Rue 2 Era Fes',
                email: 'il.moh@gmail.com ',
                indice: '43',
                city_fonc: 'Meknes',
                special_indice: 'BIS3',
                
            },
        
            {
                
                fullName: 'Irchad Ayoub',
                cin: 'MA412357PM',
                address: '7E Rue Al Karaouine, Fes',
                email: 'irchad.ayouby@gmail.com ',
                indice: 'K',
                city_fonc: 'Kenitra',
                special_indice: 'BIS5',
                
            }
            /*
            {
                
                fullName: 'Mahamet Soukaina',
                cin: 'MA2204578JH',
                address: '70 AV Rachad Narjis, Kenitra',
                email: 'soukaina.mahamat@gmail.com ',
                indice: '4F',
                city_fonc: 'Beni Melal',
                special_indice: 'BIS6',
                
            },
            {
                
                fullName: 'Aya Samadi',
                cin: 'MA85461OP',
                address: '12 Rue AV Alizee, Maamora Kenitra',
                email: 'aya.samadi@gmail.com ',
                indice: '41F',
                city_fonc: 'Rabat',
                special_indice: 'BIS7',
                
            },
            {
                
                fullName: 'Mourad Said',
                cin: 'MA2612458YK',
                address: '12e Rue Hay Rais, Tanger',
                email: 'mourad.said@gmail.com ',
                indice: '51J',
                city_fonc: 'Marrakech',
                special_indice: 'BIS8',
                
            },
            {
                
                fullName: 'Hiba Jeon',
                cin: 'MA2045132KM',
                address: 'Rue 5 Maarif Marrakech',
                email: 'hiba.jeon@gmail.com ',
                indice: '27L',
                city_fonc: 'Fes',
                special_indice: 'BIS9'
                
            }
            */
        ];

        for (let i = 0; i < titres.length; i++) {
            titres[i].docType = 'titre';
            await ctx.stub.putState('TITRE' + i, Buffer.from(JSON.stringify(titres[i])));
            console.info('Added <--> ', titres[i]);
        }
        console.info('============= END : Initialize Ledger ===========');
    }

    async queryTitre(ctx, titreNumber) {
        const titreAsBytes = await ctx.stub.getState(titreNumber); // get the titre from chaincode state
        if (!titreAsBytes || titreAsBytes.length === 0) {
            throw new Error(`${titreNumber} does not exist`);
        }
        console.log(titreAsBytes.toString());
        return titreAsBytes.toString();
    }

    async createTitre(ctx, titreNumber, fullName, cin, address, email, indice, city_fonc, special_indice) {
        console.info('============= START : Create Titre ===========');

        const titre = {
            fullName,
            docType: 'titre',
            cin,
            address,
            email,
            indice,
            city_fonc,
            special_indice
        };

        await ctx.stub.putState(titreNumber, Buffer.from(JSON.stringify(titre)));
        console.info('============= END : Create Titre ===========');
    }

    async queryAllTitres(ctx) {
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

    async changeTitreOwner(ctx, titreNumber, newOwner) {
        console.info('============= START : changeTitreOwner ===========');

        const titreAsBytes = await ctx.stub.getState(titreNumber); // get the titre from chaincode state
        if (!titreAsBytes || titreAsBytes.length === 0) {
            throw new Error(`${titreNumber} does not exist`);
        }
        const titre = JSON.parse(titreAsBytes.toString());
        titre.fullName = newOwner;

        await ctx.stub.putState(titreNumber, Buffer.from(JSON.stringify(titre)));
        console.info('============= END : changeTitreOwner ===========');
    }

}

module.exports = TitreContract;
