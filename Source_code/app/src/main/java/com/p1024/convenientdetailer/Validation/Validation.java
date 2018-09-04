package com.p1024.convenientdetailer.Validation;

import java.util.regex.Pattern;

/**
 * Created by WinDOWS on 22-12-2015.
 */
public class Validation {
   // public static final Pattern WEBSITE_URL = Pattern.compile("(www.)([a-zA-Z0-9]+).[a-zA-Z0-9]*.?[a-z]{2}.?([a-z]+)?");//(.com|.co.in)
    public static final Pattern WEBSITE_URL = Pattern.compile("(https?://?)?(www.)([a-zA-Z0-9]+).[a-zA-Z0-9]*.?[a-z]{2}.?([A-Za-z0-9@./_#&+?=-]*)?");//(.com|.co.in)

    public static final Pattern INSTAGRAM_URL = Pattern.compile("(https?://?)?(www.)?instagram.com/([A-Za-z0-9@./_#&+?=-]*)?");//(.com|.co.in)
    public static final Pattern FACEBOOKURL = Pattern.compile("(https?://?)(www.?|m.?|[A-Za-z0-9@./#&+?-]*.?)?(facebook|fb).com/([A-Za-z0-9@./_#&+?=-]*)?");//(.com|.co.in)

    public static final Pattern STRONG_PASSWORD = Pattern
            .compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    public static final Pattern PASSWORD_PATTERN = Pattern
            .compile("[A-Z0-9a-z]{5,10}");

    public static final Pattern NAME_PATTERN = Pattern
            .compile("[A-Za-z][a-zA-Z ]*$");

    public static final Pattern FIRST_NAME_PATTERN = Pattern
            .compile("[A-Z][a-zA-Z]*");

    public static final Pattern LAST_NAME_PATTERN = Pattern
            .compile("[a-zA-z]+([ '-][a-zA-Z]+)*");

    public static final Pattern PHONE_PATTERN = Pattern
            .compile("\\+[0-9]{11,13}");

    public static final Pattern CODE_PATTERN = Pattern
            .compile("[a-zA-z0-9]{1,20}");

    public static final Pattern ADDRESS_PATTERN = Pattern
            .compile("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)"
                    // "[A-Z0-9a-z-,.]"
            );


    //public static final Pattern FacebookURL =Pattern.compile("            (?:(?:http|https):\\/\\/)?(?:www.)?facebook.com\\/(?:(?:\\w)*#!\\/)?(?:pages\\/)?(?:[?\\w\\-]*\\/)?(?:profile.php\\?id=(?=\\d.*))?([\\w\\-]*)?\n");
   // public static final Pattern FacebookURL =Pattern.compile("/(?:https?:\/\/)?(?:www\.)?(mbasic.facebook|m\.facebook|facebook|fb)\.(com|me)\/(?:(?:\w\.)*#!\/)?(?:pages\/)?(?:[\w\-\.]*\/)*([\w\-\.]*)/ig");

    public static final Pattern URL = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    //public static Pattern YOUTUBE_URL_PATTERN = Pattern.compile("https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*");

   // public static Pattern YOUTUBE_URL_PATTERN = Pattern.compile("(http(s)?:\\\\/\\\\/)?(www\\\\.|m\\\\.)?youtu(be\\\\.com|\\\\.be)(\\\\/watch\\\\?([&=a-z]{0,})(v=[\\\\d\\\\w]{1,}).+|\\\\/[\\\\d\\\\w]{1,})");

    public static Pattern YOUTUBE_URL_PATTERN = Pattern.compile("^(?:https?://)?(?:www\\.)?youtu(?:\\.be|be\\.com)/(?:watch\\?v=)?([\\w-]{10,})");

    //

    public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    /**
     * Instagram profile URL
     * @param password
     * @return
     */
    public static boolean checkinstagramprofile(String email) {
        return INSTAGRAM_URL.matcher(email).matches();
    }
    /**
     * Instagram profile URL
     * @param password
     * @return
     */
    public static boolean checkfacebookprofileurl(String email) {
        return FACEBOOKURL.matcher(email).matches();
    }
    public static boolean checkPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean checkName(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }

    public static boolean checkFirstName(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }

	/*public static boolean checkLastName(String name) {
        return NAME_PATTERN.matcher(name).matches();
	}*/

    public static boolean checkPhone(String name) {
        return PHONE_PATTERN.matcher(name).matches();
    }

    public static boolean checkPhone1(String name) {
        return PHONE_PATTERN.matcher(name).matches();
    }

    public static boolean checkAddress(String address) {
        return ADDRESS_PATTERN.matcher(address).matches();
    }

    public static boolean checkCode(String code) {
        return CODE_PATTERN.matcher(code).matches();
    }

    public static boolean checkURL(String url) {
        return URL.matcher(url).matches();
    }

    public static boolean checkStrongPassword(String password) {
        return STRONG_PASSWORD.matcher(password).matches();
    }
    /*public static boolean checkfacebookUrl(String url)
    {
        return FacebookURL.matcher(url).matches();
    }*/
  //  public static final Pattern WEBSITE_URL = Pattern.compile("^(https?://)?(www\.)?([\w\Q$-_+!*'(),%\E]+\.)+[‌​\w]{2,63}\/?$");
    public static boolean validatePhoneNumber(String phoneNo) {
        // validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}"))
            return true;
            // validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;
            // validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
            return true;
            // validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
            return true;
            // return false if nothing matches the input
        else
            return false;
    }

    public static boolean validatePhoneNumber1(String phoneNo) {
        if (phoneNo.matches("/^[(]?(\\d{3})[)]?(\\d{3})[-|\\s]?(\\d{4})$/"))
            return true;
        else
            return false;
    }
    public static boolean chekWebSiteUrl(String name) {
        return WEBSITE_URL.matcher(name).matches();
    }

    public static boolean checkYoutubeUrl(String url) {
        return YOUTUBE_URL_PATTERN.matcher(url).matches();

    }

//    ^(?=.*\d)(?=.*[a-zA-Z]).*$
//    Uppercase, Lowercase and digit
}

