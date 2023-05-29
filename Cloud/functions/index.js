const functions = require("firebase-functions");
const admin = require("firebase-admin");
const serviceAccount = require("real-poetry-firebase-adminsdk-f6djk-d8966a81e0.json");
// const cors = require("cors")({origin: true});

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

//write a poem to the database
functions.uploadPoem = https.functions.onCall((data, context) => {
    //if the user is logged in
    if(context.auth !== "undefined"){
        //obtain the relevant information for the upload
        const id = context.auth.uid;
        const date = context.date;
        const title = data.title;
        const poem = data.content;

        return db.collection("Users").doc(`${id}`).doc(`${date}`).set({
            title: title,
            content: poem,
        }).then(() => {
            return 0;
        }).catch(() => {
            return 1;
        });
    }
});

//read poems from the database

//check for a new prompt and return it
