/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ms2hardcodedraft;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Merruel
 */
public class MS2HardCodeDraft {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        //Variables area
        int yer; int mon; int dte; int day; int i;
        LocalDate startday = null; LocalDate endday = null;
        LocalDate refday = null;
        LocalTime clockin; LocalTime clockout;
        LocalTime schedin;
        
        double dutyhrs; double grosspay; double subsidy; double deductions;
        double accumhrs = 0;
        double accumpay = 0;
        double lunchbreak = 1d;
        double undertime = 0;
        double graceperiod = 10d;
        double totalhrs = 0;
        double totalpay = 0;
        double totaldeduction = 0;
        double totalsubsidy = 0;
        
        //This should be dynamic depending on user's mode choice
        int empid = 10001;
        String fname = "Manuel";
        String lname = "Garcia III";
        String bday = "10-Nov-83";
        double salary = 90000.00d;
        double rice = 1500.00d;
        double phone = 2000.00d;
        double clothing = 1000.00d; 
        double hrrate = 535.71d;
        double sssprem = 0;
        double philprem = 0;
        double pagibig = 0;
        double wtax = 0;
        double totalsssprem = 0;
        double totalphilprem = 0;
        double totalpagibig = 0;
        double totalwtax = 0;
        
        //These are arrays as substitute to google sheets information
        String dutydays[] = {"03-Jun-2024", "04-Jun-2024", "05-Jun-2024", "06-Jun-2024", "07-Jun-2024",
            "10-Jun-2024", "11-Jun-2024", "12-Jun-2024", "13-Jun-2024", "14-Jun-2024",
            "17-Jun-2024", "18-Jun-2024", "19-Jun-2024", "20-Jun-2024", "21-Jun-2024",
            "24-Jun-2024", "25-Jun-2024", "26-Jun-2024", "27-Jun-2024", "28-Jun-2024",};
        
        String timein[] = {"08:59", "09:47", "10:57", "09:32", "09:46",
            "09:10", "10:30", "08:37", "08:24", "09:28",
            "09:07", "08:32", "09:05", "09:07", "08:19",
            "10:26", "09:52", "08:23", "08:46", "09:07",};
        
        String timeout[] = {"18:31", "19:07", "21:32", "19:15", "19:15",
            "18:36", "20:53", "18:25", "19:20", "19:24",
            "19:10", "18:35", "18:30", "20:01", "15:24",
            "19:43", "17:12", "18:51", "16:09", "18:28",};
        
        //Setting up formatting to be used for dates and hours
        SimpleDateFormat dateformatter = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = new GregorianCalendar();
        
        DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("HH:mm");
        schedin = LocalTime.parse("08:00", timeformatter);
        DecimalFormat df = new DecimalFormat("#.##");
        
        //Output of employee information
        System.out.println("***********************");
        System.out.println("EMPLOYEE NUMBER: " + empid);
        System.out.println("EMPLOYEE NAME: " + lname + ", " + fname);
        System.out.println("BIRTHDAY: " + bday);
        System.out.println("***********************");
        System.out.println("");
        
        Date start = dateformatter.parse("03-Jun-2024");
        cal.setTime(start);
        day = cal.get(Calendar.DAY_OF_WEEK);
        
        yer = cal.get(Calendar.YEAR);
        mon = cal.get(Calendar.MONTH) + 1;
        dte = cal.get(Calendar.DAY_OF_MONTH);
        
        LocalDate firstday = LocalDate.of(yer, mon, 1);
        LocalDate lastday = YearMonth.of(yer, mon).atEndOfMonth();
        
        day = day - 2; // 2 day of the week is Monday, setting up startday as Monday
        if (day <= 0) {
            startday = LocalDate.of(yer, mon, dte + day);
            endday = LocalDate.of(yer, mon, dte + day + 6);
        } else {
            startday = LocalDate.of(yer, mon, dte - day);
            endday = LocalDate.of(yer, mon, dte - day + 6);
        }
        
        //Loop function will be changed from array setup to google sheets end of rows
        for (i = 0; i < dutydays.length; i++) { 
            Date date = dateformatter.parse(dutydays[i]); //converts date from string to date format
            cal.setTime(date);
            //day = cal.get(Calendar.DAY_OF_WEEK); //checks what day of the week is the date (Sunday=1, Monday=2, ...)
            yer = cal.get(Calendar.YEAR);
            mon = cal.get(Calendar.MONTH) + 1;
            dte = cal.get(Calendar.DAY_OF_MONTH);
            refday = LocalDate.of(yer, mon, dte);
 
            //loop to catch dates between Monday to Sunday
            if ((refday.compareTo(startday) >= 0) && (refday.compareTo(endday) <= 0)) {
                //checking if timein time is within grace period
                clockin = LocalTime.parse(timein[i], timeformatter);
                Duration underdiff = Duration.between(clockin, schedin);

                //undertime computation with grace period considerations
                if (underdiff.toMinutes() < graceperiod * -1 ) {
                    undertime = underdiff.toMinutes() / 60d; 
                } else {
                    undertime = 0;
                }

                //computation of duty hours
                clockin = schedin;
                clockout = LocalTime.parse(timeout[i], timeformatter);
                Duration timediff = Duration.between(clockin, clockout);
                dutyhrs = timediff.toMinutes() / 60d;
                dutyhrs = dutyhrs - lunchbreak + undertime;

                //computation of grosspay
                grosspay = dutyhrs * hrrate;

                //accumulation of duty hours and grosspay per week
                accumhrs = accumhrs + dutyhrs;
                accumpay = accumpay + grosspay;
             
            } else {
                //this part will display computed accumulated data and refresh computations
                subsidy = (rice + phone + clothing) / 4;
                
                MS2HardCodeDraft sss = new MS2HardCodeDraft();
                sssprem = sss.compSSS(salary);
                sssprem = sssprem / 4;
                
                MS2HardCodeDraft phil = new MS2HardCodeDraft();
                philprem = phil.compPhil(salary);
                philprem = philprem / 4;
                
                pagibig = 200 / 4; //maximum required contribution
                
                MS2HardCodeDraft tax = new MS2HardCodeDraft();
                wtax = tax.compTax(salary);
                wtax = wtax / 4;
                
                deductions = sssprem + philprem + pagibig + wtax;
                
                System.out.println("----------------------------------------");
                System.out.println("Period: " + startday + " to " + endday);
                System.out.println("Total Hours: " + df.format(accumhrs));
                System.out.println("Hourly Rate: " + df.format(hrrate));
                System.out.println("GROSS PAY: " + df.format(accumpay));
                System.out.println("");
                System.out.println("Rice Subsidy: " + df.format(rice / 4));
                System.out.println("Phone Subsidy: " + df.format(phone / 4));
                System.out.println("Clothing Subsidy: " + df.format(clothing / 4));
                System.out.println("TOTAL SUBSIDIES: " + df.format(subsidy));
                System.out.println("");
                System.out.println("SSS: " + df.format(sssprem));
                System.out.println("Philhealth: " + df.format(philprem));
                System.out.println("Pag-ibig: " + df.format(pagibig));
                System.out.println("Withholding Tax: " + df.format(wtax));
                System.out.println("TOTAL DEDUCTIONS: " + df.format(deductions));
                System.out.println("");
                System.out.println("NET PAY: " + (df.format(accumpay + subsidy - deductions)));
                System.out.println("");
                
                //refreshes variables after displaying a week of information
                startday = refday;
                endday = LocalDate.of(yer, mon, dte + 6);
                accumhrs = 0d;
                accumpay = 0d;
                
                //computes data of first day of the week: Monday after the refresh
                clockin = LocalTime.parse(timein[i], timeformatter);
                Duration underdiff = Duration.between(clockin, schedin);

                if (underdiff.toMinutes() < graceperiod * -1 ) {
                    undertime = underdiff.toMinutes() / 60d; 
                } else {
                    undertime = 0;
                }

                //computation of duty hours
                clockin = schedin;
                clockout = LocalTime.parse(timeout[i], timeformatter);
                Duration timediff = Duration.between(clockin, clockout);
                dutyhrs = timediff.toMinutes() / 60d;
                dutyhrs = dutyhrs - lunchbreak + undertime;

                //computation of grosspay
                grosspay = dutyhrs * hrrate;

                //accumulation of duty hours and grosspay per week
                accumhrs = accumhrs + dutyhrs;
                accumpay = accumpay + grosspay;
             }
            
            //accumulating all duty hours for the month
            totalhrs = totalhrs + dutyhrs;
            totalpay = totalpay + grosspay;
            totalsubsidy = rice + phone + clothing;
            
            MS2HardCodeDraft sss = new MS2HardCodeDraft();
            totalsssprem = sss.compSSS(salary);
                
            MS2HardCodeDraft phil = new MS2HardCodeDraft();
            totalphilprem = phil.compPhil(salary);
                
            totalpagibig = 200;
                
            MS2HardCodeDraft tax = new MS2HardCodeDraft();
            totalwtax = tax.compTax(salary);

            totaldeduction = totalsssprem + totalphilprem + totalpagibig + totalwtax;
            
            //Handle last data in array or in database
            if (i == dutydays.length - 1) {
                subsidy = (rice + phone + clothing) / 4;
  
                //HardCoding sss = new HardCoding();
                sssprem = sss.compSSS(salary);
                sssprem = sssprem / 4;
                
                //HardCoding phil = new HardCoding();
                philprem = phil.compPhil(salary);
                philprem = philprem / 4;
                
                pagibig = 200 / 4;
                
                //HardCoding tax = new HardCoding();
                wtax = tax.compTax(salary);
                wtax = wtax / 4;
                
                deductions = sssprem + philprem + pagibig + wtax;
                
                System.out.println("----------------------------------------");
                System.out.println("Period: " + startday + " to " + endday);
                System.out.println("Total Hours: " + df.format(accumhrs));
                System.out.println("Hourly Rate: " + df.format(hrrate));
                System.out.println("GROSS PAY: " + df.format(accumpay));
                System.out.println("");
                System.out.println("Rice Subsidy: " + df.format(rice / 4));
                System.out.println("Phone Subsidy: " + df.format(phone / 4));
                System.out.println("Clothing Subsidy: " + df.format(clothing / 4));
                System.out.println("TOTAL SUBSIDIES: " + df.format(subsidy));
                System.out.println("");
                System.out.println("SSS: " + df.format(sssprem));
                System.out.println("Philhealth: " + df.format(philprem));
                System.out.println("Pag-ibig: " + df.format(pagibig));
                System.out.println("Withholding Tax: " + df.format(wtax));
                System.out.println("TOTAL DEDUCTIONS: " + df.format(deductions));
                System.out.println("");
                System.out.println("NET PAY: " + (df.format(accumpay + subsidy - deductions)));
                System.out.println("");
            }
        }
        
        //total month payroll computation here
        System.out.println("*******************************************");
        System.out.println("");
        System.out.println("FOR THE PERIOD: " + firstday + " TO " + lastday);
        System.out.println("TOTAL HOURS: " + df.format(totalhrs));
        System.out.println("HOURLY RATE: " + df.format(hrrate));
        System.out.println("TOTAL GROSS PAY: " + df.format(totalpay));
        System.out.println("");
        System.out.println("RICE: " + df.format(rice));
        System.out.println("PHONE: " + df.format(phone));
        System.out.println("CLOTHING: " + df.format(clothing));
        System.out.println("TOTAL SUBSIDIES: " + df.format(totalsubsidy));
        System.out.println("");
        System.out.println("SSS: " + df.format(totalsssprem));
        System.out.println("PHILHEALTH: " + df.format(totalphilprem));
        System.out.println("PAG-IBIG: " + df.format(totalpagibig));
        System.out.println("WITHHOLDING TAX: " + df.format(totalwtax));
        System.out.println("TOTAL DEDUCTIONS: " + df.format(totaldeduction));
        System.out.println("");
        System.out.println("TOTAL NET PAY: " + df.format(totalpay + totalsubsidy - totaldeduction));
        System.out.println("");
        System.out.println("*********** END OF COMPUTATION ************");
    }
    
    //public class here

    public double compSSS (double BasicSalary) {
        double SSS;
        
        if (BasicSalary > 0 && BasicSalary < 3250) {
            SSS = 135;
        } else if (BasicSalary >= 3250 && BasicSalary < 3750) {
            SSS = 157.50f;
        } else if (BasicSalary >= 3750 && BasicSalary < 4250) {
            SSS = 180;
        } else if (BasicSalary >= 4250 && BasicSalary < 4750) {
            SSS = 202.50f;
        } else if (BasicSalary >= 4750 && BasicSalary < 5250) {
            SSS = 225;
        } else if (BasicSalary >= 5250 && BasicSalary < 5750) {
            SSS = 247.50f;
        } else if (BasicSalary >= 5750 && BasicSalary < 6250) {
            SSS = 270;
        } else if (BasicSalary >= 6250 && BasicSalary < 6750) {
            SSS = 292.50f;
        } else if (BasicSalary >= 6750 && BasicSalary < 7250) {
            SSS = 315;
        } else if (BasicSalary >= 7250 && BasicSalary < 7750) {
            SSS = 337.50f;
        } else if (BasicSalary >= 7750 && BasicSalary < 8250) {
            SSS = 360;
        } else if (BasicSalary >= 8250 && BasicSalary < 8750) {
            SSS = 382.50f;
        } else if (BasicSalary >= 8750 && BasicSalary < 9250) {
            SSS = 405;
        } else if (BasicSalary >= 9250 && BasicSalary < 9750) {
            SSS = 427.50f;
        } else if (BasicSalary >= 9750 && BasicSalary < 10250) {
            SSS = 450;
        } else if (BasicSalary >= 10250 && BasicSalary < 10750) {
            SSS = 472.50f;
        } else if (BasicSalary >= 10750 && BasicSalary < 11250) {
            SSS = 495;
        } else if (BasicSalary >= 11250 && BasicSalary < 11750) {
            SSS = 517.50f;
        } else if (BasicSalary >= 11750 && BasicSalary < 12250) {
            SSS = 540;
        } else if (BasicSalary >= 12250 && BasicSalary < 12750) {
            SSS = 562.50f;
        } else if (BasicSalary >= 12250 && BasicSalary < 12750) {
            SSS = 562.50f;
        } else if (BasicSalary >= 12750 && BasicSalary < 13250) {
            SSS = 585;
        } else if (BasicSalary >= 13250 && BasicSalary < 13750) {
            SSS = 607.50f;
        } else if (BasicSalary >= 13750 && BasicSalary < 14250) {
            SSS = 630;
        } else if (BasicSalary >= 14250 && BasicSalary < 14750) {
            SSS = 652.50f;
        } else if (BasicSalary >= 14750 && BasicSalary < 15250) {
            SSS = 675;
        } else if (BasicSalary >= 15250 && BasicSalary < 15750) {
            SSS = 697.50f;
        } else if (BasicSalary >= 15750 && BasicSalary < 16250) {
            SSS = 720;
        } else if (BasicSalary >= 16250 && BasicSalary < 16750) {
            SSS = 742.50f;
        } else if (BasicSalary >= 16750 && BasicSalary < 17250) {
            SSS = 765;
        } else if (BasicSalary >= 17250 && BasicSalary < 17750) {
            SSS = 787.50f;
        } else if (BasicSalary >= 17750 && BasicSalary < 18250) {
            SSS = 810;
        } else if (BasicSalary >= 18250 && BasicSalary < 18750) {
            SSS = 832.50f;
        } else if (BasicSalary >= 18750 && BasicSalary < 19250) {
            SSS = 855;
        } else if (BasicSalary >= 19250 && BasicSalary < 19750) {
            SSS = 877.50f;
        } else if (BasicSalary >= 19750 && BasicSalary < 20250) {
            SSS = 900;
        } else if (BasicSalary >= 20250 && BasicSalary < 20750) {
            SSS = 922.50f;
        } else if (BasicSalary >= 20750 && BasicSalary < 21250) {
            SSS = 945;
        } else if (BasicSalary >= 21250 && BasicSalary < 21750) {
            SSS = 967.50f;
        } else if (BasicSalary >= 21750 && BasicSalary < 22250) {
            SSS = 990;
        } else if (BasicSalary >= 22250 && BasicSalary < 22750) {
            SSS = 1012.50f;
        } else if (BasicSalary >= 22750 && BasicSalary < 23250) {
            SSS = 1035;
        } else if (BasicSalary >= 23250 && BasicSalary < 23750) {
            SSS = 1057.50f;
        } else if (BasicSalary >= 23750 && BasicSalary < 24250) {
            SSS = 1080;
        } else if (BasicSalary >= 24250 && BasicSalary < 24750) {
            SSS = 1102.50f;
        } else if (BasicSalary >= 24750) {
            SSS = 1125;
        } else { 
            SSS = 0; //catch rouge figures
        }
        return SSS;
    }
    
    public double compPhil (double BasicSalary) {
        double Philhealth;
        if (BasicSalary <= 10000) {
            Philhealth = 300;
        } else if (BasicSalary > 10000 && BasicSalary < 60000) {
            Philhealth = (float) (Math.round((BasicSalary * 0.03) * 100.0) / 100.0);
        } else if (BasicSalary >= 60000) {
            Philhealth = 1800;
        } else { 
            Philhealth = 0; //catch rouge figures
        }
        return Philhealth;
    }
    
    public double compTax (double BasicSalary) {
        double Tax = 0;
        if (BasicSalary < 20833) {
            Tax = 0;
        } else if (BasicSalary >= 20833 && BasicSalary < 33333) {
            Tax = (float) (Math.round(((BasicSalary - 20833) * 0.20) * 100.0) / 100.0);
        } else if (BasicSalary >= 33333 && BasicSalary < 66667) {
            Tax = (float) (Math.round(((BasicSalary - 33333) * 0.25) * 100.0) / 100.0);
            Tax = Tax + 2500;
        } else if (BasicSalary >= 66667 && BasicSalary < 166667) {
            Tax = (float) (Math.round(((BasicSalary - 66667) * 0.30) * 100.0) / 100.0);
            Tax = Tax + 10833;
        } else if (BasicSalary >= 166667 && BasicSalary < 666667) {
            Tax = (float) (Math.round(((BasicSalary - 166667) * 0.32) * 100.0) / 100.0);
            Tax = Tax + 40833.33f;
        } else if (BasicSalary >= 666667) {
            Tax = (float) (Math.round(((BasicSalary - 666667) * 0.35) * 100.0) / 100.0);
            Tax = Tax + 200833.33f;
        }
        return Tax;
    }
}
