package com.researchfip.puc.mytalks.general;

public final class Auxiliar {
    private static int permissionok = 0;
    private static Auxiliar instance;

    private Auxiliar (){

    }

    public static Auxiliar getintance(){
        if(instance == null){
            instance = new Auxiliar();
        }
        return instance;
    }

    public static int getPermissionOk(){
        return permissionok;
    }

    public static void setPermissionOk(int i){
        permissionok = i;
    }

}
