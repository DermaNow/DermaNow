package com.example.ala.dermanow;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Rahaf on 3/27/2018.
 */

public class SendMail extends AsyncTask<Void, Void, Void> {

    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String email;
    private String subject;
    private String Username;
    private String message;


    //Class Constructor
    public SendMail(Context context, String email, String subject, String Username, String message) {
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.Username = Username;
        this.message = message;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mimeMessage = new MimeMessage(session);

            //Setting sender address
            mimeMessage.setFrom(new InternetAddress(Config.EMAIL));
            //Adding receiver
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mimeMessage.setSubject(subject);
            //Adding message
            //  mimeMessage.setText(message);
            String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"><head>\n" +
                    "    <!--[if gte mso 9]><xml>\n" +
                    "     <o:OfficeDocumentSettings>\n" +
                    "      <o:AllowPNG/>\n" +
                    "      <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                    "     </o:OfficeDocumentSettings>\n" +
                    "    </xml><![endif]-->\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
                    "    <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\n" +
                    "    <title></title>\n" +
                    "    <!--[if !mso]><!-- -->\n" +
                    "\t<link href=\"https://fonts.googleapis.com/css?family=Montserrat\" rel=\"stylesheet\" type=\"text/css\">\n" +
                    "\t<!--<![endif]-->\n" +
                    "    \n" +
                    "    <style type=\"text/css\" id=\"media-query\">\n" +
                    "      body {\n" +
                    "  margin: 0;\n" +
                    "  padding: 0; }\n" +
                    "\n" +
                    "table, tr, td {\n" +
                    "  vertical-align: top;\n" +
                    "  border-collapse: collapse; }\n" +
                    "\n" +
                    ".ie-browser table, .mso-container table {\n" +
                    "  table-layout: fixed; }\n" +
                    "\n" +
                    "* {\n" +
                    "  line-height: inherit; }\n" +
                    "\n" +
                    "a[x-apple-data-detectors=true] {\n" +
                    "  color: inherit !important;\n" +
                    "  text-decoration: none !important; }\n" +
                    "\n" +
                    "[owa] .img-container div, [owa] .img-container button {\n" +
                    "  display: block !important; }\n" +
                    "\n" +
                    "[owa] .fullwidth button {\n" +
                    "  width: 100% !important; }\n" +
                    "\n" +
                    "[owa] .block-grid .col {\n" +
                    "  display: table-cell;\n" +
                    "  float: none !important;\n" +
                    "  vertical-align: top; }\n" +
                    "\n" +
                    ".ie-browser .num12, .ie-browser .block-grid, [owa] .num12, [owa] .block-grid {\n" +
                    "  width: 600px !important; }\n" +
                    "\n" +
                    ".ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div {\n" +
                    "  line-height: 100%; }\n" +
                    "\n" +
                    ".ie-browser .mixed-two-up .num4, [owa] .mixed-two-up .num4 {\n" +
                    "  width: 200px !important; }\n" +
                    "\n" +
                    ".ie-browser .mixed-two-up .num8, [owa] .mixed-two-up .num8 {\n" +
                    "  width: 400px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.two-up .col, [owa] .block-grid.two-up .col {\n" +
                    "  width: 300px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.three-up .col, [owa] .block-grid.three-up .col {\n" +
                    "  width: 200px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.four-up .col, [owa] .block-grid.four-up .col {\n" +
                    "  width: 150px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.five-up .col, [owa] .block-grid.five-up .col {\n" +
                    "  width: 120px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.six-up .col, [owa] .block-grid.six-up .col {\n" +
                    "  width: 100px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.seven-up .col, [owa] .block-grid.seven-up .col {\n" +
                    "  width: 85px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.eight-up .col, [owa] .block-grid.eight-up .col {\n" +
                    "  width: 75px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.nine-up .col, [owa] .block-grid.nine-up .col {\n" +
                    "  width: 66px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.ten-up .col, [owa] .block-grid.ten-up .col {\n" +
                    "  width: 60px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.eleven-up .col, [owa] .block-grid.eleven-up .col {\n" +
                    "  width: 54px !important; }\n" +
                    "\n" +
                    ".ie-browser .block-grid.twelve-up .col, [owa] .block-grid.twelve-up .col {\n" +
                    "  width: 50px !important; }\n" +
                    "\n" +
                    "@media only screen and (min-width: 620px) {\n" +
                    "  .block-grid {\n" +
                    "    width: 600px !important; }\n" +
                    "  .block-grid .col {\n" +
                    "    vertical-align: top; }\n" +
                    "    .block-grid .col.num12 {\n" +
                    "      width: 600px !important; }\n" +
                    "  .block-grid.mixed-two-up .col.num4 {\n" +
                    "    width: 200px !important; }\n" +
                    "  .block-grid.mixed-two-up .col.num8 {\n" +
                    "    width: 400px !important; }\n" +
                    "  .block-grid.two-up .col {\n" +
                    "    width: 300px !important; }\n" +
                    "  .block-grid.three-up .col {\n" +
                    "    width: 200px !important; }\n" +
                    "  .block-grid.four-up .col {\n" +
                    "    width: 150px !important; }\n" +
                    "  .block-grid.five-up .col {\n" +
                    "    width: 120px !important; }\n" +
                    "  .block-grid.six-up .col {\n" +
                    "    width: 100px !important; }\n" +
                    "  .block-grid.seven-up .col {\n" +
                    "    width: 85px !important; }\n" +
                    "  .block-grid.eight-up .col {\n" +
                    "    width: 75px !important; }\n" +
                    "  .block-grid.nine-up .col {\n" +
                    "    width: 66px !important; }\n" +
                    "  .block-grid.ten-up .col {\n" +
                    "    width: 60px !important; }\n" +
                    "  .block-grid.eleven-up .col {\n" +
                    "    width: 54px !important; }\n" +
                    "  .block-grid.twelve-up .col {\n" +
                    "    width: 50px !important; } }\n" +
                    "\n" +
                    "@media (max-width: 620px) {\n" +
                    "  .block-grid, .col {\n" +
                    "    min-width: 320px !important;\n" +
                    "    max-width: 100% !important;\n" +
                    "    display: block !important; }\n" +
                    "  .block-grid {\n" +
                    "    width: calc(100% - 40px) !important; }\n" +
                    "  .col {\n" +
                    "    width: 100% !important; }\n" +
                    "    .col > div {\n" +
                    "      margin: 0 auto; }\n" +
                    "  img.fullwidth, img.fullwidthOnMobile {\n" +
                    "    max-width: 100% !important; }\n" +
                    "  .no-stack .col {\n" +
                    "    min-width: 0 !important;\n" +
                    "    display: table-cell !important; }\n" +
                    "  .no-stack.two-up .col {\n" +
                    "    width: 50% !important; }\n" +
                    "  .no-stack.mixed-two-up .col.num4 {\n" +
                    "    width: 33% !important; }\n" +
                    "  .no-stack.mixed-two-up .col.num8 {\n" +
                    "    width: 66% !important; }\n" +
                    "  .no-stack.three-up .col.num4 {\n" +
                    "    width: 33% !important; }\n" +
                    "  .no-stack.four-up .col.num3 {\n" +
                    "    width: 25% !important; }\n" +
                    "  .mobile_hide {\n" +
                    "    min-height: 0px;\n" +
                    "    max-height: 0px;\n" +
                    "    max-width: 0px;\n" +
                    "    display: none;\n" +
                    "    overflow: hidden;\n" +
                    "    font-size: 0px; } }\n" +
                    "\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body class=\"clean-body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #A4D9EF\">\n" +
                    "  <style type=\"text/css\" id=\"media-query-bodytag\">\n" +
                    "    @media (max-width: 520px) {\n" +
                    "      .block-grid {\n" +
                    "        min-width: 320px!important;\n" +
                    "        max-width: 100%!important;\n" +
                    "        width: 100%!important;\n" +
                    "        display: block!important;\n" +
                    "      }\n" +
                    "\n" +
                    "      .col {\n" +
                    "        min-width: 320px!important;\n" +
                    "        max-width: 100%!important;\n" +
                    "        width: 100%!important;\n" +
                    "        display: block!important;\n" +
                    "      }\n" +
                    "\n" +
                    "        .col > div {\n" +
                    "          margin: 0 auto;\n" +
                    "        }\n" +
                    "\n" +
                    "      img.fullwidth {\n" +
                    "        max-width: 100%!important;\n" +
                    "      }\n" +
                    "\t\t\timg.fullwidthOnMobile {\n" +
                    "        max-width: 100%!important;\n" +
                    "      }\n" +
                    "      .no-stack .col {\n" +
                    "\t\t\t\tmin-width: 0!important;\n" +
                    "\t\t\t\tdisplay: table-cell!important;\n" +
                    "\t\t\t}\n" +
                    "\t\t\t.no-stack.two-up .col {\n" +
                    "\t\t\t\twidth: 50%!important;\n" +
                    "\t\t\t}\n" +
                    "\t\t\t.no-stack.mixed-two-up .col.num4 {\n" +
                    "\t\t\t\twidth: 33%!important;\n" +
                    "\t\t\t}\n" +
                    "\t\t\t.no-stack.mixed-two-up .col.num8 {\n" +
                    "\t\t\t\twidth: 66%!important;\n" +
                    "\t\t\t}\n" +
                    "\t\t\t.no-stack.three-up .col.num4 {\n" +
                    "\t\t\t\twidth: 33%!important;\n" +
                    "\t\t\t}\n" +
                    "\t\t\t.no-stack.four-up .col.num3 {\n" +
                    "\t\t\t\twidth: 25%!important;\n" +
                    "\t\t\t}\n" +
                    "      .mobile_hide {\n" +
                    "        min-height: 0px!important;\n" +
                    "        max-height: 0px!important;\n" +
                    "        max-width: 0px!important;\n" +
                    "        display: none!important;\n" +
                    "        overflow: hidden!important;\n" +
                    "        font-size: 0px!important;\n" +
                    "      }\n" +
                    "    }\n" +
                    "  </style>\n" +
                    "  <!--[if IE]><div class=\"ie-browser\"><![endif]-->\n" +
                    "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
                    "  <table class=\"nl-container\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #A4D9EF;width: 100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "\t<tbody>\n" +
                    "\t<tr style=\"vertical-align: top\">\n" +
                    "\t\t<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
                    "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #A4D9EF;\"><![endif]-->\n" +
                    "\n" +
                    "    <div style=\"background-color:transparent;\">\n" +
                    "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\" class=\"block-grid \">\n" +
                    "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                    "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:transparent;\"><![endif]-->\n" +
                    "\n" +
                    "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\" width:600px; padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:0px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                    "            <div class=\"col num12\" style=\"min-width: 320px;max-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                    "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                    "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:5px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                    "\n" +
                    "                  \n" +
                    "                    &#160;\n" +
                    "                  \n" +
                    "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "              </div>\n" +
                    "            </div>\n" +
                    "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "    </div>    <div style=\"background-color:transparent;\">\n" +
                    "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #FFFFFF;\" class=\"block-grid \">\n" +
                    "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#FFFFFF;\">\n" +
                    "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:#FFFFFF;\"><![endif]-->\n" +
                    "\n" +
                    "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\" width:600px; padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                    "            <div class=\"col num12\" style=\"min-width: 320px;max-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                    "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                    "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                    "\n" +
                    "                  \n" +
                    "                    <div align=\"center\" class=\"img-container center  autowidth  \" style=\"padding-right: 0px;  padding-left: 0px;\">\n" +
                    "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px;line-height:0px;\"><td style=\"padding-right: 0px; padding-left: 0px;\" align=\"center\"><![endif]-->\n" +
                    "  <img class=\"center  autowidth \" align=\"center\" border=\"0\" src=\"https://image.ibb.co/b5qhNH/walaalogoapplication.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: block !important;border: 0;height: auto;float: none;width: 100%;max-width: 250px\" width=\"250\">\n" +
                    "<!--[if mso]></td></tr></table><![endif]-->\n" +
                    "</div>\n" +
                    "\n" +
                    "                  \n" +
                    "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "              </div>\n" +
                    "            </div>\n" +
                    "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "    </div>    <div style=\"background-color:transparent;\">\n" +
                    "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #FFFFFF;\" class=\"block-grid \">\n" +
                    "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#FFFFFF;\">\n" +
                    "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:#FFFFFF;\"><![endif]-->\n" +
                    "\n" +
                    "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\" width:600px; padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                    "            <div class=\"col num12\" style=\"min-width: 320px;max-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                    "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                    "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:0px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                    "\n" +
                    "                  \n" +
                    "                    <div class=\"\">\n" +
                    "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px;\"><![endif]-->\n" +
                    "\t<div style=\"line-height:120%;color:#90D9F6;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px;\">\t\n" +
                    "\t\t<div style=\"font-size:12px;line-height:14px;color:#90D9F6;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 14px;line-height: 17px;text-align: center\"><span style=\"font-size: 28px; line-height: 33px;\"><strong><span style=\"line-height: 33px; font-size: 28px;\">Hello " + Username + "&#160;</span></strong></span><br><br></p></div>\t\n" +
                    "\t</div>\n" +
                    "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                    "</div>\n" +
                    "                  \n" +
                    "                  \n" +
                    "                    <div class=\"\">\n" +
                    "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 20px; padding-bottom: 10px;\"><![endif]-->\n" +
                    "\t<div style=\"line-height:150%;color:#555555;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; padding-right: 10px; padding-left: 10px; padding-top: 20px; padding-bottom: 10px;\">\t\n" +
                    "\t\t<div style=\"font-size:12px;line-height:18px;color:#555555;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 14px;line-height: 21px;text-align: center\"><strong>" + message + "</strong><br></p></div>\t\n" +
                    "\t</div>\n" +
                    "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                    "</div>\n" +
                    "                  \n" +
                    "                  \n" +
                    "                    <div class=\"\">\n" +
                    "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 20px; padding-bottom: 10px;\"><![endif]-->\n" +
                    "\t<div style=\"line-height:150%;color:#0D0D0D;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; padding-right: 10px; padding-left: 10px; padding-top: 20px; padding-bottom: 10px;\">\t\n" +
                    "\t\t<div style=\"font-size:12px;line-height:18px;color:#0D0D0D;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 14px;line-height: 21px;text-align: center\">Sorry again and thanks for your&#160;understanding<br></p></div>\t\n" +
                    "\t</div>\n" +
                    "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                    "</div>\n" +
                    "                  \n" +
                    "                  \n" +
                    "                    \n" +
                    "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"divider \" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 100%;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "    <tbody>\n" +
                    "        <tr style=\"vertical-align: top\">\n" +
                    "            <td class=\"divider_inner\" style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;padding-right: 10px;padding-left: 10px;padding-top: 30px;padding-bottom: 10px;min-width: 100%;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "                <table class=\"divider_content\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 0px solid transparent;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "                    <tbody>\n" +
                    "                        <tr style=\"vertical-align: top\">\n" +
                    "                            <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "                                <span></span>\n" +
                    "                            </td>\n" +
                    "                        </tr>\n" +
                    "                    </tbody>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </tbody>\n" +
                    "</table>\n" +
                    "                  \n" +
                    "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "              </div>\n" +
                    "            </div>\n" +
                    "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "    </div>    <div style=\"background-color:transparent;\">\n" +
                    "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #525252;\" class=\"block-grid three-up \">\n" +
                    "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#525252;\">\n" +
                    "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:#525252;\"><![endif]-->\n" +
                    "\n" +
                    "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"200\" style=\" width:200px; padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                    "            <div class=\"col num4\" style=\"max-width: 320px;min-width: 200px;display: table-cell;vertical-align: top;\">\n" +
                    "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                    "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                    "\n" +
                    "                  \n" +
                    "                    <div class=\"\">\n" +
                    "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top: 20px; padding-bottom: 0px;\"><![endif]-->\n" +
                    "\t<div style=\"line-height:120%;color:#90D9F6;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; padding-right: 0px; padding-left: 0px; padding-top: 20px; padding-bottom: 0px;\">\t\n" +
                    "\t\t<div style=\"font-size:12px;line-height:14px;color:#90D9F6;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 12px;line-height: 14px;text-align: center\"><br></p></div>\t\n" +
                    "\t</div>\n" +
                    "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                    "</div>\n" +
                    "                  \n" +
                    "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "              </div>\n" +
                    "            </div>\n" +
                    "              <!--[if (mso)|(IE)]></td><td align=\"center\" width=\"200\" style=\" width:200px; padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                    "            <div class=\"col num4\" style=\"max-width: 320px;min-width: 200px;display: table-cell;vertical-align: top;\">\n" +
                    "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                    "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                    "\n" +
                    "                  \n" +
                    "                    <div class=\"\">\n" +
                    "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top: 10px; padding-bottom: 0px;\"><![endif]-->\n" +
                    "\t<div style=\"line-height:150%;color:#90D9F6;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; padding-right: 0px; padding-left: 0px; padding-top: 10px; padding-bottom: 0px;\">\t\n" +
                    "\t\t<div style=\"font-size:12px;line-height:18px;color:#90D9F6;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 12px;line-height: 18px;text-align: center\"><span style=\"font-size: 12px; line-height: 18px;\"><strong>Email</strong> <span style=\"color: rgb(255, 255, 255); line-height: 18px; font-size: 12px;\">Dermanowteam@gmail.com</span></span></p></div>\t\n" +
                    "\t</div>\n" +
                    "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                    "</div>\n" +
                    "                  \n" +
                    "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "              </div>\n" +
                    "            </div>\n" +
                    "              <!--[if (mso)|(IE)]></td><td align=\"center\" width=\"200\" style=\" width:200px; padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                    "            <div class=\"col num4\" style=\"max-width: 320px;min-width: 200px;display: table-cell;vertical-align: top;\">\n" +
                    "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                    "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                    "\n" +
                    "                  \n" +
                    "                    <div class=\"\">\n" +
                    "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top: 20px; padding-bottom: 0px;\"><![endif]-->\n" +
                    "\t<div style=\"line-height:120%;color:#90D9F6;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; padding-right: 0px; padding-left: 0px; padding-top: 20px; padding-bottom: 0px;\">\t\n" +
                    "\t\t<div style=\"font-size:12px;line-height:14px;color:#90D9F6;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 12px;line-height: 14px\"><br data-mce-bogus=\"1\"></p></div>\t\n" +
                    "\t</div>\n" +
                    "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                    "</div>\n" +
                    "                  \n" +
                    "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "              </div>\n" +
                    "            </div>\n" +
                    "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "    </div>    <div style=\"background-color:transparent;\">\n" +
                    "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\" class=\"block-grid \">\n" +
                    "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                    "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:transparent;\"><![endif]-->\n" +
                    "\n" +
                    "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\" width:600px; padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                    "            <div class=\"col num12\" style=\"min-width: 320px;max-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                    "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                    "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:0px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                    "\n" +
                    "                  \n" +
                    "                    \n" +
                    "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"divider \" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 100%;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "    <tbody>\n" +
                    "        <tr style=\"vertical-align: top\">\n" +
                    "            <td class=\"divider_inner\" style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;padding-right: 30px;padding-left: 30px;padding-top: 30px;padding-bottom: 30px;min-width: 100%;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "                <table class=\"divider_content\" height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 0px solid transparent;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "                    <tbody>\n" +
                    "                        <tr style=\"vertical-align: top\">\n" +
                    "                            <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                    "                                <span>&#160;</span>\n" +
                    "                            </td>\n" +
                    "                        </tr>\n" +
                    "                    </tbody>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </tbody>\n" +
                    "</table>\n" +
                    "                  \n" +
                    "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                    "              </div>\n" +
                    "            </div>\n" +
                    "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "    </div>   <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                    "\t\t</td>\n" +
                    "  </tr>\n" +
                    "  </tbody>\n" +
                    "  </table>\n" +
                    "  <!--[if (mso)|(IE)]></div><![endif]-->\n" +
                    "\n" +
                    "\n" +
                    "</body></html>";
            mimeMessage.setContent(html, "text/html; charset=utf-8");

            //Sending email
            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
