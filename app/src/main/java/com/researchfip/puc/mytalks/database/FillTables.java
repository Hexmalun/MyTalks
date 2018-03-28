package com.researchfip.puc.mytalks.database;

import android.content.Context;

/**
 * Created by Mateus on 11/06/2016.
 */
public class FillTables {
    private Context C;

    private final String[] [] conteCodes = { {"724",	"0",	"br",	"Brasil",	"55",	"Nextel (Telet)"},
                                        {"724",	"1",	"br",	"Brasil",	"55",	"Vivo S.A./Telemig"},
                                        {"724",	"2",	"br",	"Brasil",	"55",	"TIM"},
                                        {"724",	"3",	"br",	"Brasil",	"55",	"TIM"},
                                        {"724",	"4",	"br",	"Brasil",	"55",	"TIM"},
                                        {"724",	"5",	"br",	"Brasil",	"55",	"Claro/Albra/America Movil"},
                                        {"724",	"6",	"br",	"Brasil",	"55",	"Vivo S.A./Telemig"},
                                        {"724",	"7",	"br",	"Brasil",	"55",	"CTBC/Triangulo"},
                                        {"724",	"8",	"br",	"Brasil",	"55",	"TIM"},
                                        {"724",	"10",	"br",	"Brasil",	"55",	"Vivo S.A./Telemig"},
                                        {"724",	"11",	"br",	"Brasil",	"55",	"Vivo S.A./Telemig"},
                                        {"724",	"12",	"br",	"Brasil",	"55",	"Claro/Albra/America Movil"},
                                        {"724",	"15",	"br",	"Brasil",	"55",	"Sercontel Cel"},
                                        {"724",	"16",	"br",	"Brasil",	"55",	"Brasil Telcom"},
                                        {"724",	"19",	"br",	"Brasil",	"55",	"Vivo S.A./Telemig"},
                                        {"724",	"23",	"br",	"Brasil",	"55",	"Vivo S.A./Telemig"},
                                        {"724",	"24",	"br",	"Brasil",	"55",	"Amazonia Celular S/A"},
                                        {"724",	"30",	"br",	"Brasil",	"55",	"Oi (TNL PCS / Oi)"},
                                        {"724",	"31",	"br",	"Brasil",	"55",	"Oi (TNL PCS / Oi)"},
                                        {"724",	"32",	"br",	"Brasil",	"55",	"CTBC Celular SA (CTBC)"},
                                        {"724",	"33",	"br",	"Brasil",	"55",	"CTBC Celular SA (CTBC)"},
                                        {"724",	"34",	"br",	"Brasil",	"55",	"CTBC Celular SA (CTBC)"},
                                        {"724",	"37",	"br",	"Brasil",	"55",	"Unicel do Brasil Telecomunicacoes Ltda"},
                                        {"724",	"38",	"br",	"Brasil",	"55",	"Claro/Albra/America Movil"},
                                        {"724",	"39",	"br",	"Brasil",	"55",	"Nextel (Telet)"},
                                        {"724",	"54",	"br",	"Brasil",	"55",	"PORTO SEGURO TELECOMUNICACOES"}};

    public String[] [] getContCodes(){
        return conteCodes;
    }
    public String getName(String c){
        for(int i = 0; i < conteCodes.length; i++){
            if(c.equals(conteCodes[i][1])){
                return(conteCodes[i][5]);
            }
        }
        return("");
    }




}
