package com.gongfutrip.api.web.pricing;

import com.gongfutrip.other.DateUtil;

import java.util.Calendar;

/**
 * @author Konson.zhao
 * @date 2015/10/10 16:29
 * @Description
 */
public class RandomOD {

    private static String[] ods = {"SYD,PVG"};
//        {"SYD,PVG","SYD,CNS","SYD,CAN","SIN,CAN","SIN,WUH\n"
//            + "SIN,PEK","SIN,CSX","SIN,CKG","SIN,CGO\n"
//            + "PVG,AKL","MEL,HKG","MEL,HTI","MEL,PVG\n"
//            + "MEL,CAN","ICN,PEK","ICN,PVG","HKG,SYD\n"
//            + "CHC,AKL","BNE,HKG","BNE,PVG","AKL,HKG\n"
//            + "AKL,PVG","ADL,PVG","HKG,PEK","HKG,CTU\n"
//            + "HKG,DPS","HKG,BKK","LHR,PEK","LHR,SHA\n"
//            + "HKG,SIN","HKG,SFO","HKG,LHR","SYD,HKG\n"
//            + "SIN,HFE","SIN,FOC","SIN,LHW","SIN,NNG\n"
//            + "SIN,KWE","SIN,SJW","SIN,HRB","SIN,CGQ\n"
//            + "SIN,NKG","SIN,KHN","SIN,HET","SIN,XNN\n"
//            + "SIN,TYN","SIN,CTU","SIN,LXA","SIN,URC\n"
//            + "SIN,KMG","SIN,HKG","HKG,HGH","HKG,SHA\n"
//            + "HKG,FOC","NKG,HKG","DPS,HKG","BKK,HKG\n"
//            + "BKK,PEK","HND,PEK","CAN,NRT","HKG,SPK\n"
//            + "BOS,PVG","NRT,DLC","TSN,NGO","HKG,PNH\n"
//            + "HKG,NGB","HKG,PEN","HKG,KMG","HKG,OKA\n"
//            + "BKK,SHA","HKG,URC","HAK,HKG","HKG,TSN\n"
//            + "HKG,HET","HKG,MDL","HKG,LED","HKG,ULN\n"
//            + "HKG,YNZ","JKT,SHA","GMP,PEK","GMP,SHA\n"
//            + "ICN,CAN","SYD,LAX","SYD,SFO","SYD,SEA\n"
//            + "SYD,ORD","SYD,YVR","SYD,YOW","SYD,YYZ\n"
//            + "SYD,AKL","SYD,CGK","SYD,KUL","SYD,SIN\n"
//            + "SYD,MNL","SYD,ICN","SYD,GMP","SYD,NRT\n"
//            + "SYD,HND","SYD,HAN","SYD,SGN","SYD,VTE\n"
//            + "SYD,RGN","SYD,MDL","SYD,BKK","SYD,CNX\n"
//            + "SYD,HKT","SYD,USM","SYD,PNH","SYD,DPS\n"
//            + "SYD,KTM","SYD,ULN","SYD,DEL","SYD,BOM\n"
//            + "SYD,MLE","SYD,CMB","SYD,DOH","SYD,DXB\n"
//            + "SYD,AUH","SYD,IST","SYD,SVO","SYD,IEV\n"
//            + "SYD,MSQ","SYD,WAW","SYD,PRG","SYD,BUD\n"
//            + "SYD,CPH","SYD,HEL","SYD,OSL","SYD,STO\n"
//            + "SYD,TXL","SYD,FRA","SYD,MUC","SYD,HAM\n"
//            + "SYD,LHR","SYD,CDG","SYD,AMS","SYD,BRU\n"
//            + "SYD,MAD","SYD,ATH","SYD,VIE","SYD,BRN\n"
//            + "SYD,GVA","SYD,ZRH","SYD,ROM","SYD,MIL\n"
//            + "SYD,VCE","SYD,ROR","SYD,SPN","SYD,GUM\n"
//            + "SYD,JNB","SYD,CPT","SYD,PEK","SYD,BNE\n"
//            + "SYD,MEL","SYD,TPE","MEL,LAX","MEL,SFO\n"
//            + "MEL,SEA","MEL,ORD","MEL,YVR","MEL,YYZ\n"
//            + "MEL,AKL","MEL,CGK","MEL,SIN","MEL,MNL\n"
//            + "MEL,ICN","MEL,GMP","MEL,NRT","MEL,HND\n"
//            + "MEL,HAN","MEL,SGN","MEL,VTE","MEL,RGN\n"
//            + "MEL,MDL","MEL,BKK","MEL,CNX","MEL,HKT\n"
//            + "MEL,USM","MEL,PNH","MEL,DPS","MEL,KTM\n"
//            + "MEL,ULN","MEL,DEL","MEL,BOM","MEL,MLE\n"
//            + "MEL,CMB","MEL,DOH","MEL,DXB","MEL,AUH\n"
//            + "MEL,IST","MEL,SVO","MEL,IEV","MEL,MSQ\n"
//            + "MEL,WAW","MEL,PRG","MEL,BUD","MEL,CPH\n"
//            + "MEL,HEL","MEL,OSL","MEL,STO","MEL,TXL\n"
//            + "MEL,FRA","MEL,MUC","MEL,HAM","MEL,LHR\n"
//            + "MEL,CDG","MEL,AMS","MEL,BRU","MEL,MAD\n"
//            + "MEL,ATH","MEL,VIE","MEL,BRN","MEL,GVA\n"
//            + "MEL,ZRH","MEL,ROM","MEL,MIL","MEL,VCE\n"
//            + "MEL,ROR","MEL,SPN","MEL,GUM","MEL,JNB\n"
//            + "MEL,CPT","BNE,CAN","MEL,PEK","MEL,BNE\n"
//            + "MEL,SYD","MEL,TPE","SIN,ATH","SIN,AUH\n"
//            + "SIN,BRN","SIN,BRU","SIN,BUD","SIN,CPH\n"
//            + "SIN,CPT","SIN,DEL","SIN,DOH","SIN,DPS\n"
//            + "SIN,GMP","SIN,GUM","SIN,GVA","SIN,HAM\n"
//            + "SIN,HEL","SIN,HND","SIN,IEV","SIN,KTM\n"
//            + "SIN,LAX","SIN,MAD","SIN,MDL","SIN,MEL\n"
//            + "SIN,MIL","SIN,MSQ","SIN,MUC","SIN,ORD\n"
//            + "SIN,OSL","SIN,PNH","SIN,PRG","SIN,PVG\n"
//            + "SIN,RGN","SIN,ROM","SIN,ROR","SIN,SEA\n"
//            + "SIN,SFO","SIN,SVO","SIN,SPN","SIN,STO\n"
//            + "SIN,SYD","SIN,TXL","SIN,ULN","SIN,VCE\n"
//            + "SIN,VIE","SIN,WAW","SIN,YOW","SIN,YVR\n"
//            + "SIN,YYZ","SIN,ZRH","HKG,NAN","HKG,TPE\n"
//            + "HKG,HEL","HKG,AKL","HKG,AMS","HKG,BNE\n"
//            + "HKG,BOM","HKG,BRN","HKG,BRU","HKG,BUD\n"
//            + "HKG,CGK","HKG,CMB","HKG,CNX","HKG,CPT\n"
//            + "HKG,DEL","HKG,DXB","HKG,FRA","HKG,GMP\n"
//            + "HKG,GVA","HKG,HAM","HKG,HND","HKG,ICN\n"
//            + "HKG,IEV","HKG,IST","HKG,JNB","HKG,MAD\n"
//            + "HKG,MLE","HKG,MNL","HKG,MSQ","HKG,OSL\n"
//            + "HKG,PRG","HKG,PVG","HKG,SGN","HKG,SVO\n"
//            + "HKG,STO","HKG,TXL","HKG,VCE","HKG,VIE\n"
//            + "HKG,WAW","HKG,YOW","HKG,YVR","HKG,YHZ\n"
//            + "HKG,YYJ","CAN,YVR","YVR,HKG","YHZ,HKG\n"
//            + "YYJ,HKG","YYZ,HKG","YVR,CAN","YYZ,PEK\n"
//            + "LAX,YYZ","HKG,KIX","BKK,UTP","BKK,CEI\n"
//            + "USM,BKK","PEK,HKG","PVG,NRT","PEK,SIN\n"
//            + "PVG,ICN","PVG,LAX","CAN,SIN","PEK,LAX\n"
//            + "SZX,BKK","PEK,CDG","PEK,REP","PEK,HKT\n"
//            + "PVG,SYD","CKG,HKG","PVG,LHR","NRT,HKG\n"
//            + "PEK,SVO","BKK,HKT","CTU,HKG","PVG,MEL\n"
//            + "PEK,SYD","HGH,HKG","PEK,DAD","PVG,JFK\n"
//            + "LHR,HKG","LHR,CTU","MXP,PEK","FCO,PEK\n"
//            + "SYD,HNL","HNL,SYD","NRT,PVG","KIX,HKG\n"
//            + "HKG,FUK","FUK,HKG","NRT,PEK","NRT,HKG\n"
//            + "NRT,CTU","NRT,SHE","NRT,CAN","NRT,XMN\n"
//            + "NRT,TAO","NRT,OKA","ICN,HKG","ICN,TAO\n"
//            + "ICN,YNJ","ICN,DLC","ICN,CTU","ICN,SHE\n"
//            + "ICN,HRB","ICN,CGQ","ICN,XIY","ICN,SPN\n"
//            + "ICN,WEH","ICN,TNA","ICN,WUH","ICN,YNT\n"
//            + "ICN,TSN","ICN,NKG","ICN,KMG","ICN,SZX\n"
//            + "ICN,CGO","ICN,HGH","ICN,CKG","ICN,NRT\n"
//            + "ICN,HND","NRT,ICN","HND,GMP","HND,ICN\n"
//            + "ICN,NRT","GMP,HND"};

    public static String getRandomOD() {
        return ods[((int) (Math.random() * ods.length))];
    }

    public static String getRandomDate(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_YEAR, (int) (Math.random() * 30));
        return DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
    }
}
