const functions = require("firebase-functions");
const admin = require("firebase-admin");
const serviceAccount = require("real-poetry-firebase-adminsdk-f6djk-d8966a81e0.json");
// const cors = require("cors")({origin: true});    //dont know if ill need this at some point

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

//write a poem to the database
/*
Fields in "data":
    title: title of the poem to upload
    content: contents of the poem to upload
*/
exports.uploadPoem = functions.https.onCall((data, context) => {
    //if the user is logged in
    if (context.auth === 'undefined') {
        return { code: 1, message: "Failed: Cannot upload poems if not logged in" };
    }

    //obtain the relevant information for the upload
    const id = context.auth.uid;
    const date = context.date;
    const title = data.title;
    const poem = data.content;

    return db.collection("Users").doc(`${id}`).collection("Poems").doc(`${date}`).set({
        title: title,
        content: poem,
    }).then(() => {
        return { code: 0, message: "Success: Poem Uploaded" };
    }).catch((error) => {
        return { code: 1, message: "Failed: Error occurred when uploading poem", error: error };
    });
});

//read poems from the database
/**
 * Fields in data:
 *      function: which set of poems you want to retrive
 *          FRND: friends poems - for the current date
 *          USR: this user's poems
 *          SPFC: a specific poem
 *      date: date of the poem you want - only relevant with SPFC call
 */
exports.getPoems = functions.https.onCall((data, context) => {
    if (context.auth === 'undefined') {
        return { code: 1, message: "Failed: Cannot retrieve poems when not logged in" };
    }

    const id = context.auth.uid;
    const callType = data.function;

    switch (callType) {
        case "FRND":
            return db.collection("Users").doc(`${id}`).collection("Friends").get().then((friendsCollection) => {
                friendsCollection.forEach((friend) => {
                    //go to each friends poem doc and retrieve todays poem if it exists or just return the set of friends poems for today whichever of these is more efficient
                });
            }).catch((error) => {
                return { code: 1, message: "Failed: Error occurred while retrieving Friends collection", error: error };
            });
        case "USR":
            return db.collection("Users").doc(`${id}`).collection("Poems").get().then((poems) => {
                let dates = [];
                poems.forEach((poem) => {
                    dates.push(poem.id);
                });
                return { code: 0, message: "Success: Dates retrieved", data: dates };
            }).catch((error) => {
                return { code: 1, message: "Failed: Error occurred when retrieving poems collection", error: error };
            });
        case "SPFC":
            const date = data.date;

            if(date === 'undefined' || date === null){
                return { code: 1, message: "Failed: Undefined or Null date" };
            }

            return db.collection("Users").doc(`${id}`).collection("Poems").doc(`${date}`).get().then((poem) => {
                return { code: 0, message: "Success: Poem retrieved", data: poem };
            }).catch((error) => {
                return { code: 1, message: "Failed: Error occurred when retrieving poem", error: error };
            });
        default:
            return { code: 1, message: `Failed: Invalid function code ${callType}` };
    }
});

//check for a new prompt and return it

//register a user in the database upon first login
