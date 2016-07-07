# Firbase-Auth-Database

**Firebase :**
* a powerful platform for building iOS, Android, and web-based apps, offering real-time data storage and synchronization, user authentication, and more.
* Firebase is a mobile platform that helps you quickly develop high-quality apps, grow your user base, and earn more money. Firebase is made up of complementary features that you can mix-and-match to fit your needs.

Firebase Email-Password Authentication and Database Store and Retrieve

##Authenticate with Firebase##

**Step 1:**
Add Firebase to Your Project:

* Create Firebase Project in the Firebase Console(https://console.firebase.google.com/)
* Add Firebase to your Android App (json file downloaded during you create project)

* Add the SDK
* First, add rules to your root-level build.gradle file, to include the google-services plugin:
```
buildscript {
    // ...
    dependencies {
        // ...
        classpath 'com.google.gms:google-services:3.0.0'
    }
}
```
* Then, in your module Gradle file (usually the app/build.gradle), add the apply plugin line at the bottom of the file to enable the Gradle plugin:
```
apply plugin: 'com.android.application'

android {
  // ...
}

dependencies {
  // ...
}

// ADD THIS AT THE BOTTOM
apply plugin: 'com.google.gms.google-services'
```
**Step 2:**
* Add the dependency for Firebase Authentication to your app-level build.gradle file:
```
compile 'com.google.firebase:firebase-auth:9.2.0'
```
**Step 3:**
* Enable Email/Password sign-in:
* In the Firebase console, open the Auth section.

**Step 4:**
* Create Password based Account:
* a)
Get the shared instance of the FirebaseAuth object:
```
private FirebaseAuth mAuth;
// ...
mAuth = FirebaseAuth.getInstance();
```
* b) Set up an AuthStateListener that responds to changes in the user's sign-in state: 
```
private FirebaseAuth.AuthStateListener mAuthListener;

// ...

@Override
protected void onCreate(Bundle savedInstanceState) {
    // ...
    mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };
    // ...
}

@Override
public void onStart() {
    super.onStart();
    mAuth.addAuthStateListener(mAuthListener);
}

@Override
public void onStop() {
    super.onStop();
    if (mAuthListener != null) {
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
```
* c) Create a new account by passing the new user's email address and password to
```
mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

                // ...
            }
        });

```
**Step 5: **
* Sign in user with an email and password:

* a) Check Weather User is already Authenticated or not
```
@Override
protected void onCreate(Bundle savedInstanceState) {
    // ...
    mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };
    // ...
}
```
* b) Check User entered email and password is valid or not
```
mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail", task.getException());
                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

                // ...
            }
        });
```

**Step 6:**
To sign out a user, call signOut:
```
FirebaseAuth.getInstance().signOut();
```
**Step 7:**
Store Data
```
DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
.............
 User user=new User(name, number);
 mDatabase.child("users").push().setValue(user);
```

**Step 8:**
Read Data Once
```
DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
..........

ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                HashMap<String,HashMap<String,HashMap<String,String>>> hashMap= (HashMap<String,HashMap<String,HashMap<String,String>>>) dataSnapshot.getValue();
                HashMap<String,HashMap<String,String>> hashMap1=hashMap.get("users");
                Log.i("TAG",hashMap.size()+"");
                Set<String> keySet=hashMap1.keySet();
                userList.clear();
                for (String key:keySet) {
                    HashMap<String,String> hashMap2=hashMap1.get(key);
                    User user=new User(hashMap2.get("name"),hashMap2.get("number"));
                    userList.add(user);
                    adapter.notifyDataSetChanged();
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

mDatabase.addValueEventListener(postListener);
```
