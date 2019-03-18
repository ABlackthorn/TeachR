package com.teachr.teachr;

public class Utils {
    private static final String FIREBASE_ENTRY = "entry";
    private static final String FIREBASE_SUBJECT = "subject";
    private static final String FIREBASE_USER = "users";
    private static final int TYPE_PROFESSEUR = 0;
    private static final int TYPE_ETUDIANT = 1;
    private static final int TYPE_OFFRE = 0;
    private static final int TYPE_DEMANDE = 0;



    public static String getFirebaseEntry() {
        return FIREBASE_ENTRY;
    }

    public static String getFirebaseSubject() {
        return FIREBASE_SUBJECT;
    }

    public static String getFirebaseUser() {
        return FIREBASE_USER;
    }

    public static int getTypeProfesseur() { return TYPE_PROFESSEUR;}

    public static int getTypeEtudiant() { return TYPE_ETUDIANT;}

    public static int getTypeOffre() { return TYPE_OFFRE;}

    public static int getTypeDemande() { return TYPE_DEMANDE;}
}
