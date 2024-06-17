package com.desirecodes.codepicker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * Created by hbb20 on 11/1/16.
 */
public class CCPCountry implements Comparable<CCPCountry> {
    static int DEFAULT_FLAG_RES = -99;
    static String TAG = "Class Country";
    static CountryCodePicker.Language loadedLibraryMasterListLanguage;
    static String dialogTitle, searchHintMessage, noResultFoundAckMessage;
    static List<CCPCountry> loadedLibraryMaterList;
    //countries with +1
    private static String ANTIGUA_AND_BARBUDA_AREA_CODES = "268";
    private static String ANGUILLA_AREA_CODES = "264";
    private static String BARBADOS_AREA_CODES = "246";
    private static String BERMUDA_AREA_CODES = "441";
    private static String BAHAMAS_AREA_CODES = "242";
    private static String CANADA_AREA_CODES = "204/226/236/249/250/289/306/343/365/403/416/418/431/437/438/450/506/514/519/579/581/587/600/604/613/639/647/705/709/769/778/780/782/807/819/825/867/873/902/905/";
    private static String DOMINICA_AREA_CODES = "767";
    private static String DOMINICAN_REPUBLIC_AREA_CODES = "809/829/849";
    private static String GRENADA_AREA_CODES = "473";
    private static String JAMAICA_AREA_CODES = "876";
    private static String SAINT_KITTS_AND_NEVIS_AREA_CODES = "869";
    private static String CAYMAN_ISLANDS_AREA_CODES = "345";
    private static String SAINT_LUCIA_AREA_CODES = "758";
    private static String MONTSERRAT_AREA_CODES = "664";
    private static String PUERTO_RICO_AREA_CODES = "787";
    private static String SINT_MAARTEN_AREA_CODES = "721";
    private static String TURKS_AND_CAICOS_ISLANDS_AREA_CODES = "649";
    private static String TRINIDAD_AND_TOBAGO_AREA_CODES = "868";
    private static String SAINT_VINCENT_AND_THE_GRENADINES_AREA_CODES = "784";
    private static String BRITISH_VIRGIN_ISLANDS_AREA_CODES = "284";
    private static String US_VIRGIN_ISLANDS_AREA_CODES = "340";

    //countries with +44
    private static String ISLE_OF_MAN = "1624";
    String nameCode;
    String phoneCode;
    String name, englishName;
    int flagResID = DEFAULT_FLAG_RES;

    public CCPCountry() {

    }

    public CCPCountry(String nameCode, String phoneCode, String name, int flagResID) {
        this.nameCode = nameCode.toUpperCase(Locale.US);
        this.phoneCode = phoneCode;
        this.name = name;
        this.flagResID = flagResID;
    }

    static CountryCodePicker.Language getLoadedLibraryMasterListLanguage() {
        return loadedLibraryMasterListLanguage;
    }

    static void setLoadedLibraryMasterListLanguage(CountryCodePicker.Language loadedLibraryMasterListLanguage) {
        CCPCountry.loadedLibraryMasterListLanguage = loadedLibraryMasterListLanguage;
    }

    public static List<CCPCountry> getLoadedLibraryMaterList() {
        return loadedLibraryMaterList;
    }

    static void setLoadedLibraryMaterList(List<CCPCountry> loadedLibraryMaterList) {
        CCPCountry.loadedLibraryMaterList = loadedLibraryMaterList;
    }

    /**
     * This function parses the raw/countries.xml file, and get list of all the countries.
     *
     * @param context: required to access application resources (where country.xml is).
     * @return List of all the countries available in xml file.
     */
    static void loadDataFromXML(Context context, CountryCodePicker.Language language) {
        List<CCPCountry> countries = new ArrayList<CCPCountry>();
        String tempDialogTitle = "", tempSearchHint = "", tempNoResultAck = "";
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlFactoryObject.newPullParser();
            InputStream ins = context.getResources().openRawResource(context.getResources()
                    .getIdentifier("ccp_" + language.toString().toLowerCase(Locale.ROOT),
                            "raw", context.getPackageName()));
            xmlPullParser.setInput(ins, "UTF-8");
            int event = xmlPullParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = xmlPullParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("country")) {
                            CCPCountry ccpCountry = new CCPCountry();
                            ccpCountry.setNameCode(xmlPullParser.getAttributeValue(null, "name_code").toUpperCase(Locale.US));
                            ccpCountry.setPhoneCode(xmlPullParser.getAttributeValue(null, "phone_code"));
                            ccpCountry.setEnglishName(xmlPullParser.getAttributeValue(null, "english_name"));
                            ccpCountry.setName(xmlPullParser.getAttributeValue(null, "name"));
                            countries.add(ccpCountry);
                        } else if (name.equals("ccp_dialog_title")) {
                            tempDialogTitle = xmlPullParser.getAttributeValue(null, "translation");
                        } else if (name.equals("ccp_dialog_search_hint_message")) {
                            tempSearchHint = xmlPullParser.getAttributeValue(null, "translation");
                        } else if (name.equals("ccp_dialog_no_result_ack_message")) {
                            tempNoResultAck = xmlPullParser.getAttributeValue(null, "translation");
                        }
                        break;
                }
                event = xmlPullParser.next();
            }
            loadedLibraryMasterListLanguage = language;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        //if anything went wrong, countries will be loaded for english language
        if (countries.size() == 0) {
            loadedLibraryMasterListLanguage = CountryCodePicker.Language.ENGLISH;
            countries = getLibraryMasterCountriesEnglish();
        }

        dialogTitle = tempDialogTitle.length() > 0 ? tempDialogTitle : "Select a country";
        searchHintMessage = tempSearchHint.length() > 0 ? tempSearchHint : "Search...";
        noResultFoundAckMessage = tempNoResultAck.length() > 0 ? tempNoResultAck : "Results not found";
        loadedLibraryMaterList = countries;

        // sort list
        Collections.sort(loadedLibraryMaterList);
    }

    public static String getDialogTitle(Context context, CountryCodePicker.Language language) {
        if (loadedLibraryMasterListLanguage == null || loadedLibraryMasterListLanguage != language || dialogTitle == null || dialogTitle.length() == 0) {
            loadDataFromXML(context, language);
        }
        return dialogTitle;
    }

    public static String getSearchHintMessage(Context context, CountryCodePicker.Language language) {
        if (loadedLibraryMasterListLanguage == null || loadedLibraryMasterListLanguage != language || searchHintMessage == null || searchHintMessage.length() == 0) {
            loadDataFromXML(context, language);
        }
        return searchHintMessage;
    }

    public static String getNoResultFoundAckMessage(Context context, CountryCodePicker.Language language) {
        if (loadedLibraryMasterListLanguage == null || loadedLibraryMasterListLanguage != language || noResultFoundAckMessage == null || noResultFoundAckMessage.length() == 0) {
            loadDataFromXML(context, language);
        }
        return noResultFoundAckMessage;
    }

    public static void setDialogTitle(String dialogTitle) {
        CCPCountry.dialogTitle = dialogTitle;
    }

    public static void setSearchHintMessage(String searchHintMessage) {
        CCPCountry.searchHintMessage = searchHintMessage;
    }

    public static void setNoResultFoundAckMessage(String noResultFoundAckMessage) {
        CCPCountry.noResultFoundAckMessage = noResultFoundAckMessage;
    }

    /**
     * Search a country which matches @param code.
     *
     * @param context
     * @param preferredCountries is list of preference countries.
     * @param code               phone code. i.e "91" or "1"
     * @return Country that has phone code as @param code.
     * or returns null if no country matches given code.
     * if same code (e.g. +1) available for more than one country ( US, canada) , this function will return preferred country.
     */
    public static CCPCountry getCountryForCode(Context context, CountryCodePicker.Language language, List<CCPCountry> preferredCountries, String code) {

        /**
         * check in preferred countries
         */
        if (preferredCountries != null && !preferredCountries.isEmpty()) {
            for (CCPCountry CCPCountry : preferredCountries) {
                if (CCPCountry.getPhoneCode().equals(code)) {
                    return CCPCountry;
                }
            }
        }

        for (CCPCountry CCPCountry : getLibraryMasterCountryList(context, language)) {
            if (CCPCountry.getPhoneCode().equals(code)) {
                return CCPCountry;
            }
        }
        return null;
    }

    /**
     * @param code phone code. i.e "91" or "1"
     * @return Country that has phone code as @param code.
     * or returns null if no country matches given code.
     * if same code (e.g. +1) available for more than one country ( US, canada) , this function will return preferred country.
     * @avoid Search a country which matches @param code. This method is just to support correct preview
     */
    static CCPCountry getCountryForCodeFromEnglishList(String code) {

        List<CCPCountry> countries;
        countries = getLibraryMasterCountriesEnglish();

        for (CCPCountry ccpCountry : countries) {
            if (ccpCountry.getPhoneCode().equals(code)) {
                return ccpCountry;
            }
        }
        return null;
    }

    static List<CCPCountry> getCustomMasterCountryList(Context context, CountryCodePicker codePicker) {
        codePicker.refreshCustomMasterList();
        if (codePicker.customMasterCountriesList != null && codePicker.customMasterCountriesList.size() > 0) {
            return codePicker.getCustomMasterCountriesList();
        } else {
            return getLibraryMasterCountryList(context, codePicker.getLanguageToApply());
        }
    }

    /**
     * Search a country which matches @param nameCode.
     *
     * @param context
     * @param customMasterCountriesList
     * @param nameCode                  country name code. i.e US or us or Au. See countries.xml for all code names.
     * @return Country that has country name code as @param code.
     */
    static CCPCountry getCountryForNameCodeFromCustomMasterList(Context context, List<CCPCountry> customMasterCountriesList, CountryCodePicker.Language language, String nameCode) {
        if (customMasterCountriesList == null || customMasterCountriesList.size() == 0) {
            return getCountryForNameCodeFromLibraryMasterList(context, language, nameCode);
        } else {
            for (CCPCountry ccpCountry : customMasterCountriesList) {
                if (ccpCountry.getNameCode().equalsIgnoreCase(nameCode)) {
                    return ccpCountry;
                }
            }
        }
        return null;
    }

    /**
     * Search a country which matches @param nameCode.
     *
     * @param context
     * @param nameCode country name code. i.e US or us or Au. See countries.xml for all code names.
     * @return Country that has country name code as @param code.
     * or returns null if no country matches given code.
     */
    public static CCPCountry getCountryForNameCodeFromLibraryMasterList(Context context, CountryCodePicker.Language language, String nameCode) {
        List<CCPCountry> countries;
        countries = CCPCountry.getLibraryMasterCountryList(context, language);
        for (CCPCountry ccpCountry : countries) {
            if (ccpCountry.getNameCode().equalsIgnoreCase(nameCode)) {
                return ccpCountry;
            }
        }
        return null;
    }

    /**
     * Search a country which matches @param nameCode.
     * This searches through local english name list. This should be used only for the preview purpose.
     *
     * @param nameCode country name code. i.e US or us or Au. See countries.xml for all code names.
     * @return Country that has country name code as @param code.
     * or returns null if no country matches given code.
     */
    static CCPCountry getCountryForNameCodeFromEnglishList(String nameCode) {
        List<CCPCountry> countries;
        countries = getLibraryMasterCountriesEnglish();
        for (CCPCountry CCPCountry : countries) {
            if (CCPCountry.getNameCode().equalsIgnoreCase(nameCode)) {
                return CCPCountry;
            }
        }
        return null;
    }

    /**
     * Search a country which matches @param code.
     *
     * @param context
     * @param preferredCountries list of country with priority,
     * @param code               phone code. i.e 91 or 1
     * @return Country that has phone code as @param code.
     * or returns null if no country matches given code.
     */
    static CCPCountry getCountryForCode(Context context, CountryCodePicker.Language language, List<CCPCountry> preferredCountries, int code) {
        return getCountryForCode(context, language, preferredCountries, code + "");
    }

    /**
     * Finds country code by matching substring from left to right from full number.
     * For example. if full number is +819017901357
     * function will ignore "+" and try to find match for first character "8"
     * if any country found for code "8", will return that country. If not, then it will
     * try to find country for "81". and so on till first 3 characters ( maximum number of characters in country code is 3).
     *
     * @param context
     * @param preferredCountries countries of preference
     * @param fullNumber         full number ( "+" (optional)+ country code + carrier number) i.e. +819017901357 / 819017901357 / 918866667722
     * @return Country JP +81(Japan) for +819017901357 or 819017901357
     * Country IN +91(India) for  918866667722
     * null for 2956635321 ( as neither of "2", "29" and "295" matches any country code)
     */
    static CCPCountry getCountryForNumber(Context context, CountryCodePicker.Language language, List<CCPCountry> preferredCountries, String fullNumber) {
        int firstDigit;
        if (fullNumber == null) {
            return null;
        } else {
            fullNumber = fullNumber.trim();
        }

        if (fullNumber.length() != 0) {
            if (fullNumber.charAt(0) == '+') {
                firstDigit = 1;
            } else {
                firstDigit = 0;
            }
            CCPCountry ccpCountry = null;
            for (int i = firstDigit; i <= fullNumber.length(); i++) {
                String code = fullNumber.substring(firstDigit, i);
                CCPCountryGroup countryGroup = null;
                try {
                    countryGroup = CCPCountryGroup.getCountryGroupForPhoneCode(Integer.parseInt(code));
                } catch (Exception ignored) {
                }
                if (countryGroup != null) {
                    int areaCodeStartsAt = firstDigit + code.length();
                    //when phone number covers area code too.
                    if (fullNumber.length() >= areaCodeStartsAt + countryGroup.areaCodeLength) {
                        String areaCode = fullNumber.substring(areaCodeStartsAt, areaCodeStartsAt + countryGroup.areaCodeLength);
                        return countryGroup.getCountryForAreaCode(context, language, areaCode);
                    } else {
                        return getCountryForNameCodeFromLibraryMasterList(context, language, countryGroup.defaultNameCode);
                    }
                } else {
                    ccpCountry = CCPCountry.getCountryForCode(context, language, preferredCountries, code);
                    if (ccpCountry != null) {
                        return ccpCountry;
                    }
                }
            }
        }
        //it reaches here means, phone number has some problem.
        return null;
    }

    /**
     * Finds country code by matching substring from left to right from full number.
     * For example. if full number is +819017901357
     * function will ignore "+" and try to find match for first character "8"
     * if any country found for code "8", will return that country. If not, then it will
     * try to find country for "81". and so on till first 3 characters ( maximum number of characters in country code is 3).
     *
     * @param context
     * @param fullNumber full number ( "+" (optional)+ country code + carrier number) i.e. +819017901357 / 819017901357 / 918866667722
     * @return Country JP +81(Japan) for +819017901357 or 819017901357
     * Country IN +91(India) for  918866667722
     * null for 2956635321 ( as neither of "2", "29" and "295" matches any country code)
     */
    public static CCPCountry getCountryForNumber(Context context, CountryCodePicker.Language language, String fullNumber) {
        return getCountryForNumber(context, language, null, fullNumber);
    }

    /**
     * Returns image res based on country name code
     *
     * @param CCPCountry
     * @return
     */
    static int getFlagMasterResID(CCPCountry CCPCountry) {
        switch (CCPCountry.getNameCode().toLowerCase()) {
            //this should be sorted based on country name code.
            case "ad": //andorra
                return R.drawable.ad_rectangle;
            case "ae": //united arab emirates
                return R.drawable.ae_rectangle;
            case "af": //afghanistan
                return R.drawable.af_rectangle;
            case "ag": //antigua & barbuda
                return R.drawable.ag_rectangle;
            case "ai": //anguilla // Caribbean Islands
                return R.drawable.ai_rectangle;
            case "al": //albania
                return R.drawable.al_rectangle;
            case "am": //armenia
                return R.drawable.am_rectangle;
            case "ao": //angola
                return R.drawable.ao_rectangle;
            case "aq": //antarctica // custom
                return R.drawable.aq_rectangle;
            case "ar": //argentina
                return R.drawable.ar_rectangle;
            case "as": //American Samoa
                return R.drawable.as_rectangle;
            case "at": //austria
                return R.drawable.at_rectangle;
            case "au": //australia
                return R.drawable.au_rectangle;
            case "aw": //aruba
                return R.drawable.aw_rectangle;
            case "ax": //alan islands
                return R.drawable.ax_rectangle;
            case "az": //azerbaijan
                return R.drawable.az_rectangle;
            case "ba": //bosnia and herzegovina
                return R.drawable.ba_rectangle;
            case "bb": //barbados
                return R.drawable.bb_rectangle;
            case "bd": //bangladesh
                return R.drawable.bd_rectangle;
            case "be": //belgium
                return R.drawable.be_rectangle;
            case "bf": //burkina faso
                return R.drawable.bf_rectangle;
            case "bg": //bulgaria
                return R.drawable.bg_rectangle;
            case "bh": //bahrain
                return R.drawable.bh_rectangle;
            case "bi": //burundi
                return R.drawable.bi_rectangle;
            case "bj": //benin
                return R.drawable.bj_rectangle;
            case "bl": //saint barthélemy
                return R.drawable.bl_rectangle;// custom
            case "bm": //bermuda
                return R.drawable.bm_rectangle;
            case "bn": //brunei darussalam // custom
                return R.drawable.bn_rectangle;
            case "bo": //bolivia, plurinational state of
                return R.drawable.bo_rectangle;
            case "br": //brazil
                return R.drawable.br_rectangle;
            case "bs": //bahamas
                return R.drawable.bs_rectangle;
            case "bt": //bhutan
                return R.drawable.bt_rectangle;
            case "bw": //botswana
                return R.drawable.bw_rectangle;
            case "by": //belarus
                return R.drawable.by_rectangle;
            case "bz": //belize
                return R.drawable.bz_rectangle;
            case "ca": //canada
                return R.drawable.ca_rectangle;
            case "cc": //cocos (keeling) islands
                return R.drawable.cc_rectangle;// custom
            case "cd": //congo, the democratic republic of the
                return R.drawable.cd_rectangle;
            case "cf": //central african republic
                return R.drawable.cf_rectangle;
            case "cg": //congo
                return R.drawable.cg_rectangle;
            case "ch": //switzerland
                return R.drawable.ch_rectangle;
            case "ci": //côte d\'ivoire
                return R.drawable.ci_rectangle;
            case "ck": //cook islands
                return R.drawable.ck_rectangle;
            case "cl": //chile
                return R.drawable.cl_rectangle;
            case "cm": //cameroon
                return R.drawable.cm_rectangle;
            case "cn": //china
                return R.drawable.cn_rectangle;
            case "co": //colombia
                return R.drawable.co_rectangle;
            case "cr": //costa rica
                return R.drawable.cr_rectangle;
            case "cu": //cuba
                return R.drawable.cu_rectangle;
            case "cv": //cape verde
                return R.drawable.cv_rectangle;
            case "cw": //curaçao
                return R.drawable.cw_rectangle;
            case "cx": //christmas island
                return R.drawable.cx_rectangle;
            case "cy": //cyprus
                return R.drawable.cy_rectangle;
            case "cz": //czech republic
                return R.drawable.cz_rectangle;
            case "de": //germany
                return R.drawable.de_rectangle;
            case "dj": //djibouti
                return R.drawable.dj_rectangle;
            case "dk": //denmark
                return R.drawable.dk_rectangle;
            case "dm": //dominica
                return R.drawable.dm_rectangle;
            case "do": //dominican republic
                return R.drawable.do_rectangle;
            case "dz": //algeria
                return R.drawable.dz_rectangle;
            case "ec": //ecuador
                return R.drawable.ec_rectangle;
            case "ee": //estonia
                return R.drawable.ee_rectangle;
            case "eg": //egypt
                return R.drawable.eg_rectangle;
            case "er": //eritrea
                return R.drawable.er_rectangle;
            case "es": //spain
                return R.drawable.es_rectangle;
            case "et": //ethiopia
                return R.drawable.et_rectangle;
            case "fi": //finland
                return R.drawable.fi_rectangle;
            case "fj": //fiji
                return R.drawable.fj_rectangle;
            case "fk": //falkland islands (malvinas)
                return R.drawable.fk_rectangle;
            case "fm": //micronesia, federated states of
                return R.drawable.fm_rectangle;
            case "fo": //faroe islands
                return R.drawable.fo_rectangle;
            case "fr": //france
                return R.drawable.fr_rectangle;
            case "ga": //gabon
                return R.drawable.ga_rectangle;
            case "gb": //united kingdom
                return R.drawable.gb_rectangle;
            case "gd": //grenada
                return R.drawable.gd_rectangle;
            case "ge": //georgia
                return R.drawable.ge_rectangle;
            case "gf": //guyane
                return R.drawable.gf_rectangle;
            case "gg": //Guernsey
                return R.drawable.gg_rectangle;
            case "gh": //ghana
                return R.drawable.gh_rectangle;
            case "gi": //gibraltar
                return R.drawable.gi_rectangle;
            case "gl": //greenland
                return R.drawable.gl_rectangle;
            case "gm": //gambia
                return R.drawable.gm_rectangle;
            case "gn": //guinea
                return R.drawable.gn_rectangle;
            case "gp": //guadeloupe
                return R.drawable.gp_rectangle;
            case "gq": //equatorial guinea
                return R.drawable.gq_rectangle;
            case "gr": //greece
                return R.drawable.gr_rectangle;
            case "gt": //guatemala
                return R.drawable.gt_rectangle;
            case "gu": //Guam
                return R.drawable.gu_rectangle;
            case "gw": //guinea-bissau
                return R.drawable.gw_rectangle;
            case "gy": //guyana
                return R.drawable.gy_rectangle;
            case "hk": //hong kong
                return R.drawable.hk_rectangle;
            case "hn": //honduras
                return R.drawable.hn_rectangle;
            case "hr": //croatia
                return R.drawable.hr_rectangle;
            case "ht": //haiti
                return R.drawable.ht_rectangle;
            case "hu": //hungary
                return R.drawable.hu_rectangle;
            case "id": //indonesia
                return R.drawable.id_rectangle;
            case "ie": //ireland
                return R.drawable.ie_rectangle;
            case "il": //israel
                return R.drawable.il_rectangle;
            case "im": //isle of man
                return R.drawable.im_rectangle; // custom
            case "is": //Iceland
                return R.drawable.is_rectangle;
            case "in": //india
                return R.drawable.in_rectangle;
            case "io": //British indian ocean territory
                return R.drawable.io_rectangle;
            case "iq": //iraq
                return R.drawable.iq_rectangle;
            case "ir": //iran, islamic republic of
                return R.drawable.ir_rectangle;
            case "it": //italy
                return R.drawable.it_rectangle;
            case "je": //Jersey
                return R.drawable.je_rectangle;
            case "jm": //jamaica
                return R.drawable.jm_rectangle;
            case "jo": //jordan
                return R.drawable.jo_rectangle;
            case "jp": //japan
                return R.drawable.jp_rectangle;
            case "ke": //kenya
                return R.drawable.ke_rectangle;
            case "kg": //kyrgyzstan
                return R.drawable.kg_rectangle;
            case "kh": //cambodia
                return R.drawable.kh_rectangle;
            case "ki": //kiribati
                return R.drawable.ki_rectangle;
            case "km": //comoros
                return R.drawable.km_rectangle;
            case "kn": //st kitts & nevis
                return R.drawable.kn_rectangle;
            case "kp": //north korea
                return R.drawable.kp_rectangle;
            case "kr": //south korea
                return R.drawable.kr_rectangle;
            case "kw": //kuwait
                return R.drawable.kw_rectangle;
            case "ky": //Cayman_Islands
                return R.drawable.ky_rectangle;
            case "kz": //kazakhstan
                return R.drawable.kz_rectangle;
            case "la": //lao people\'s democratic republic
                return R.drawable.la_rectangle;
            case "lb": //lebanon
                return R.drawable.lb_rectangle;
            case "lc": //st lucia
                return R.drawable.lc_rectangle;
            case "li": //liechtenstein
                return R.drawable.li_rectangle;
            case "lk": //sri lanka
                return R.drawable.lk_rectangle;
            case "lr": //liberia
                return R.drawable.lr_rectangle;
            case "ls": //lesotho
                return R.drawable.ls_rectangle;
            case "lt": //lithuania
                return R.drawable.lt_rectangle;
            case "lu": //luxembourg
                return R.drawable.lu_rectangle;
            case "lv": //latvia
                return R.drawable.lv_rectangle;
            case "ly": //libya
                return R.drawable.ly_rectangle;
            case "ma": //morocco
                return R.drawable.ma_rectangle;
            case "mc": //monaco
                return R.drawable.mc_rectangle;
            case "md": //moldova, republic of
                return R.drawable.md_rectangle;
            case "me": //montenegro
                return R.drawable.me_rectangle;// custom
            case "mf":
                return R.drawable.mf_rectangle;
            case "mg": //madagascar
                return R.drawable.mg_rectangle;
            case "mh": //marshall islands
                return R.drawable.mh_rectangle;
            case "mk": //macedonia, the former yugoslav republic of
                return R.drawable.mk_rectangle;
            case "ml": //mali
                return R.drawable.ml_rectangle;
            case "mm": //myanmar
                return R.drawable.mm_rectangle;
            case "mn": //mongolia
                return R.drawable.mn_rectangle;
            case "mo": //macao
                return R.drawable.mo_rectangle;
            case "mp": // Northern mariana islands
                return R.drawable.mp_rectangle;
            case "mq": //martinique
                return R.drawable.mq_rectangle;
            case "mr": //mauritania
                return R.drawable.mr_rectangle;
            case "ms": //montserrat
                return R.drawable.mq_rectangle;
            case "mt": //malta
                return R.drawable.mt_rectangle;
            case "mu": //mauritius
                return R.drawable.mu_rectangle;
            case "mv": //maldives
                return R.drawable.mv_rectangle;
            case "mw": //malawi
                return R.drawable.mw_rectangle;
            case "mx": //mexico
                return R.drawable.mx_rectangle;
            case "my": //malaysia
                return R.drawable.my_rectangle;
            case "mz": //mozambique
                return R.drawable.mz_rectangle;
            case "na": //namibia
                return R.drawable.na_rectangle;
            case "nc": //new caledonia
                return R.drawable.nc_rectangle;// custom
            case "ne": //niger
                return R.drawable.ne_rectangle;
            case "nf": //Norfolk
                return R.drawable.nf_rectangle;
            case "ng": //nigeria
                return R.drawable.ng_rectangle;
            case "ni": //nicaragua
                return R.drawable.ni_rectangle;
            case "nl": //netherlands
                return R.drawable.nl_rectangle;
            case "no": //norway
                return R.drawable.no_rectangle;
            case "np": //nepal
                return R.drawable.np_rectangle;
            case "nr": //nauru
                return R.drawable.nr_rectangle;
            case "nu": //niue
                return R.drawable.nu_rectangle;
            case "nz": //new zealand
                return R.drawable.nz_rectangle;
            case "om": //oman
                return R.drawable.om_rectangle;
            case "pa": //panama
                return R.drawable.pa_rectangle;
            case "pe": //peru
                return R.drawable.pe_rectangle;
            case "pf": //french polynesia
                return R.drawable.pf_rectangle;
            case "pg": //papua new guinea
                return R.drawable.pg_rectangle;
            case "ph": //philippines
                return R.drawable.ph_rectangle;
            case "pk": //pakistan
                return R.drawable.pk_rectangle;
            case "pl": //poland
                return R.drawable.pl_rectangle;
            case "pm": //saint pierre and miquelon
                return R.drawable.pm_rectangle;
            case "pn": //pitcairn
                return R.drawable.pn_rectangle;
            case "pr": //puerto rico
                return R.drawable.pr_rectangle;
            case "ps": //palestine
                return R.drawable.ps_rectangle;
            case "pt": //portugal
                return R.drawable.pt_rectangle;
            case "pw": //palau
                return R.drawable.pw_rectangle;
            case "py": //paraguay
                return R.drawable.py_rectangle;
            case "qa": //qatar
                return R.drawable.qa_rectangle;
            case "re": //la reunion
                return R.drawable.re_rectangle; // no exact flag found
            case "ro": //romania
                return R.drawable.ro_rectangle;
            case "rs": //serbia
                return R.drawable.rs_rectangle; // custom
            case "ru": //russian federation
                return R.drawable.ru_rectangle;
            case "rw": //rwanda
                return R.drawable.rw_rectangle;
            case "sa": //saudi arabia
                return R.drawable.sa_rectangle;
            case "sb": //solomon islands
                return R.drawable.sb_rectangle;
            case "sc": //seychelles
                return R.drawable.sc_rectangle;
            case "sd": //sudan
                return R.drawable.sd_rectangle;
            case "se": //sweden
                return R.drawable.se_rectangle;
            case "sg": //singapore
                return R.drawable.sg_rectangle;
            case "sh": //saint helena, ascension and tristan da cunha
                return R.drawable.sh_rectangle; // custom
            case "si": //slovenia
                return R.drawable.si_rectangle;
            case "sk": //slovakia
                return R.drawable.sk_rectangle;
            case "sl": //sierra leone
                return R.drawable.sl_rectangle;
            case "sm": //san marino
                return R.drawable.sm_rectangle;
            case "sn": //senegal
                return R.drawable.sn_rectangle;
            case "so": //somalia
                return R.drawable.so_rectangle;
            case "sr": //suriname
                return R.drawable.sr_rectangle;
            case "ss": //south sudan
                return R.drawable.ss_rectangle;
            case "st": //sao tome and principe
                return R.drawable.st_rectangle;
            case "sv": //el salvador
                return R.drawable.sv_rectangle;
            case "sx": //sint maarten
                return R.drawable.sx_rectangle;
            case "sy": //syrian arab republic
                return R.drawable.sy_rectangle;
            case "sz": //swaziland
                return R.drawable.sz_rectangle;
            case "tc": //turks & caicos islands
                return R.drawable.tc_rectangle;
            case "td": //chad
                return R.drawable.td_rectangle;
            case "tg": //togo
                return R.drawable.tg_rectangle;
            case "th": //thailand
                return R.drawable.th_rectangle;
            case "tj": //tajikistan
                return R.drawable.tj_rectangle;
            case "tk": //tokelau
                return R.drawable.tk_rectangle; // custom
            case "tl": //timor-leste
                return R.drawable.tl_rectangle;
            case "tm": //turkmenistan
                return R.drawable.tm_rectangle;
            case "tn": //tunisia
                return R.drawable.tn_rectangle;
            case "to": //tonga
                return R.drawable.to_rectangle;
            case "tr": //turkey
                return R.drawable.tr_rectangle;
            case "tt": //trinidad & tobago
                return R.drawable.tt_rectangle;
            case "tv": //tuvalu
                return R.drawable.tv_rectangle;
            case "tw": //taiwan, province of china
                return R.drawable.tw_rectangle;
            case "tz": //tanzania, united republic of
                return R.drawable.tz_rectangle;
            case "ua": //ukraine
                return R.drawable.ua_rectangle;
            case "ug": //uganda
                return R.drawable.ug_rectangle;
            case "us": //united states
                return R.drawable.us_rectangle;
            case "uy": //uruguay
                return R.drawable.uy_rectangle;
            case "uz": //uzbekistan
                return R.drawable.uz_rectangle;
            case "va": //holy see (vatican city state)
                return R.drawable.va_rectangle;
            case "vc": //st vincent & the grenadines
                return R.drawable.vc_rectangle;
            case "ve": //venezuela, bolivarian republic of
                return R.drawable.ve_rectangle;
            case "vg": //british virgin islands
                return R.drawable.vg_rectangle;
            case "vi": //us virgin islands
                return R.drawable.vi_rectangle;
            case "vn": //vietnam
                return R.drawable.vn_rectangle;
            case "vu": //vanuatu
                return R.drawable.vu_rectangle;
            case "wf": //wallis and futuna
                return R.drawable.wf_rectangle;
            case "ws": //samoa
                return R.drawable.ws_rectangle;
            case "xk": //kosovo
                return R.drawable.xk_rectangle;
            case "ye": //yemen
                return R.drawable.ye_rectangle;
            case "yt": //mayotte
                return R.drawable.yt_rectangle; // no exact flag found
            case "za": //south africa
                return R.drawable.za_rectangle;
            case "zm": //zambia
                return R.drawable.zm_rectangle;
            case "zw": //zimbabwe
                return R.drawable.zw_rectangle;
            default:
                return R.drawable.flag_transparent;
        }
    }


    static int getCircleFlagMasterResID(CCPCountry CCPCountry) {
        switch (CCPCountry.getNameCode().toLowerCase()) {
            //this should be sorted based on country name code.
            case "ad": //andorra
                return R.drawable.ad_circle;
            case "ae": //united arab emirates
                return R.drawable.ae_circle;
            case "af": //afghanistan
                return R.drawable.af_circle;
            case "ag": //antigua & barbuda
                return R.drawable.ag_circle;
            case "ai": //anguilla // Caribbean Islands
                return R.drawable.ai_circle;
            case "al": //albania
                return R.drawable.al_circle;
            case "am": //armenia
                return R.drawable.am_circle;
            case "ao": //angola
                return R.drawable.ao_circle;
            case "aq": //antarctica // custom
                return R.drawable.aq_circle;
            case "ar": //argentina
                return R.drawable.ar_circle;
            case "as": //American Samoa
                return R.drawable.as_circle;
            case "at": //austria
                return R.drawable.at_circle;
            case "au": //australia
                return R.drawable.au_circle;
            case "aw": //aruba
                return R.drawable.aw_circle;
            case "ax": //alan islands
                return R.drawable.ax_circle;
            case "az": //azerbaijan
                return R.drawable.az_circle;
            case "ba": //bosnia and herzegovina
                return R.drawable.ba_circle;
            case "bb": //barbados
                return R.drawable.bb_circle;
            case "bd": //bangladesh
                return R.drawable.bd_circle;
            case "be": //belgium
                return R.drawable.be_circle;
            case "bf": //burkina faso
                return R.drawable.bf_circle;
            case "bg": //bulgaria
                return R.drawable.bg_circle;
            case "bh": //bahrain
                return R.drawable.bh_circle;
            case "bi": //burundi
                return R.drawable.bi_circle;
            case "bj": //benin
                return R.drawable.bj_circle;
            case "bl": //saint barthélemy
                return R.drawable.bl_circle;// custom
            case "bm": //bermuda
                return R.drawable.bm_circle;
            case "bn": //brunei darussalam // custom
                return R.drawable.bn_circle;
            case "bo": //bolivia, plurinational state of
                return R.drawable.bo_circle;
            case "br": //brazil
                return R.drawable.br_circle;
            case "bs": //bahamas
                return R.drawable.bs_circle;
            case "bt": //bhutan
                return R.drawable.bt_circle;
            case "bw": //botswana
                return R.drawable.bw_circle;
            case "by": //belarus
                return R.drawable.by_circle;
            case "bz": //belize
                return R.drawable.bz_circle;
            case "ca": //canada
                return R.drawable.ca_circle;
            case "cc": //cocos (keeling) islands
                return R.drawable.cc_circle;// custom
            case "cd": //congo, the democratic republic of the
                return R.drawable.cd_circle;
            case "cf": //central african republic
                return R.drawable.cf_circle;
            case "cg": //congo
                return R.drawable.cg_circle;
            case "ch": //switzerland
                return R.drawable.ch_circle;
            case "ci": //côte d\'ivoire
                return R.drawable.ci_circle;
            case "ck": //cook islands
                return R.drawable.ck_circle;
            case "cl": //chile
                return R.drawable.cl_circle;
            case "cm": //cameroon
                return R.drawable.cm_circle;
            case "cn": //china
                return R.drawable.cn_circle;
            case "co": //colombia
                return R.drawable.co_circle;
            case "cr": //costa rica
                return R.drawable.cr_circle;
            case "cu": //cuba
                return R.drawable.cu_circle;
            case "cv": //cape verde
                return R.drawable.cv_circle;
            case "cw": //curaçao
                return R.drawable.cw_circle;
            case "cx": //christmas island
                return R.drawable.cx_circle;
            case "cy": //cyprus
                return R.drawable.cy_circle;
            case "cz": //czech republic
                return R.drawable.cz_circle;
            case "de": //germany
                return R.drawable.de_circle;
            case "dj": //djibouti
                return R.drawable.dj_circle;
            case "dk": //denmark
                return R.drawable.dk_circle;
            case "dm": //dominica
                return R.drawable.dm_circle;
            case "do": //dominican republic
                return R.drawable.do_circle;
            case "dz": //algeria
                return R.drawable.dz_circle;
            case "ec": //ecuador
                return R.drawable.ec_circle;
            case "ee": //estonia
                return R.drawable.ee_circle;
            case "eg": //egypt
                return R.drawable.eg_circle;
            case "er": //eritrea
                return R.drawable.er_circle;
            case "es": //spain
                return R.drawable.es_circle;
            case "et": //ethiopia
                return R.drawable.et_circle;
            case "fi": //finland
                return R.drawable.fi_circle;
            case "fj": //fiji
                return R.drawable.fj_circle;
            case "fk": //falkland islands (malvinas)
                return R.drawable.fk_circle;
            case "fm": //micronesia, federated states of
                return R.drawable.fm_circle;
            case "fo": //faroe islands
                return R.drawable.fo_circle;
            case "fr": //france
                return R.drawable.fr_circle;
            case "ga": //gabon
                return R.drawable.ga_circle;
            case "gb": //united kingdom
                return R.drawable.gb_circle;
            case "gd": //grenada
                return R.drawable.gd_circle;
            case "ge": //georgia
                return R.drawable.ge_circle;
            case "gf": //guyane
                return R.drawable.gf_circle;
            case "gg": //Guernsey
                return R.drawable.gg_circle;
            case "gh": //ghana
                return R.drawable.gh_circle;
            case "gi": //gibraltar
                return R.drawable.gi_circle;
            case "gl": //greenland
                return R.drawable.gl_circle;
            case "gm": //gambia
                return R.drawable.gm_circle;
            case "gn": //guinea
                return R.drawable.gn_circle;
            case "gp": //guadeloupe
                return R.drawable.gp_circle;
            case "gq": //equatorial guinea
                return R.drawable.gq_circle;
            case "gr": //greece
                return R.drawable.gr_circle;
            case "gt": //guatemala
                return R.drawable.gt_circle;
            case "gu": //Guam
                return R.drawable.gu_circle;
            case "gw": //guinea-bissau
                return R.drawable.gw_circle;
            case "gy": //guyana
                return R.drawable.gy_circle;
            case "hk": //hong kong
                return R.drawable.hk_circle;
            case "hn": //honduras
                return R.drawable.hn_circle;
            case "hr": //croatia
                return R.drawable.hr_circle;
            case "ht": //haiti
                return R.drawable.ht_circle;
            case "hu": //hungary
                return R.drawable.hu_circle;
            case "id": //indonesia
                return R.drawable.id_circle;
            case "ie": //ireland
                return R.drawable.ie_circle;
            case "il": //israel
                return R.drawable.il_circle;
            case "im": //isle of man
                return R.drawable.im_circle; // custom
            case "is": //Iceland
                return R.drawable.is_circle;
            case "in": //india
                return R.drawable.in_circle;
            case "io": //British indian ocean territory
                return R.drawable.io_circle;
            case "iq": //iraq
                return R.drawable.iq_circle;
            case "ir": //iran, islamic republic of
                return R.drawable.ir_circle;
            case "it": //italy
                return R.drawable.it_circle;
            case "je": //Jersey
                return R.drawable.je_circle;
            case "jm": //jamaica
                return R.drawable.jm_circle;
            case "jo": //jordan
                return R.drawable.jo_circle;
            case "jp": //japan
                return R.drawable.jp_circle;
            case "ke": //kenya
                return R.drawable.ke_circle;
            case "kg": //kyrgyzstan
                return R.drawable.kg_circle;
            case "kh": //cambodia
                return R.drawable.kh_circle;
            case "ki": //kiribati
                return R.drawable.ki_circle;
            case "km": //comoros
                return R.drawable.km_circle;
            case "kn": //st kitts & nevis
                return R.drawable.kn_circle;
            case "kp": //north korea
                return R.drawable.kp_circle;
            case "kr": //south korea
                return R.drawable.kr_circle;
            case "kw": //kuwait
                return R.drawable.kw_circle;
            case "ky": //Cayman_Islands
                return R.drawable.ky_circle;
            case "kz": //kazakhstan
                return R.drawable.kz_circle;
            case "la": //lao people\'s democratic republic
                return R.drawable.la_circle;
            case "lb": //lebanon
                return R.drawable.lb_circle;
            case "lc": //st lucia
                return R.drawable.lc_circle;
            case "li": //liechtenstein
                return R.drawable.li_circle;
            case "lk": //sri lanka
                return R.drawable.lk_circle;
            case "lr": //liberia
                return R.drawable.lr_circle;
            case "ls": //lesotho
                return R.drawable.ls_circle;
            case "lt": //lithuania
                return R.drawable.lt_circle;
            case "lu": //luxembourg
                return R.drawable.lu_circle;
            case "lv": //latvia
                return R.drawable.lv_circle;
            case "ly": //libya
                return R.drawable.ly_circle;
            case "ma": //morocco
                return R.drawable.ma_circle;
            case "mc": //monaco
                return R.drawable.mc_circle;
            case "md": //moldova, republic of
                return R.drawable.md_circle;
            case "me": //montenegro
                return R.drawable.me_circle;// custom
            case "mf":
                return R.drawable.mf_circle;
            case "mg": //madagascar
                return R.drawable.mg_circle;
            case "mh": //marshall islands
                return R.drawable.mh_circle;
            case "mk": //macedonia, the former yugoslav republic of
                return R.drawable.mk_circle;
            case "ml": //mali
                return R.drawable.ml_circle;
            case "mm": //myanmar
                return R.drawable.mm_circle;
            case "mn": //mongolia
                return R.drawable.mn_circle;
            case "mo": //macao
                return R.drawable.mo_circle;
            case "mp": // Northern mariana islands
                return R.drawable.mp_circle;
            case "mq": //martinique
                return R.drawable.mq_circle;
            case "mr": //mauritania
                return R.drawable.mr_circle;
            case "ms": //montserrat
                return R.drawable.mq_circle;
            case "mt": //malta
                return R.drawable.mt_circle;
            case "mu": //mauritius
                return R.drawable.mu_circle;
            case "mv": //maldives
                return R.drawable.mv_circle;
            case "mw": //malawi
                return R.drawable.mw_circle;
            case "mx": //mexico
                return R.drawable.mx_circle;
            case "my": //malaysia
                return R.drawable.my_circle;
            case "mz": //mozambique
                return R.drawable.mz_circle;
            case "na": //namibia
                return R.drawable.na_circle;
            case "nc": //new caledonia
                return R.drawable.nc_circle;// custom
            case "ne": //niger
                return R.drawable.ne_circle;
            case "nf": //Norfolk
                return R.drawable.nf_circle;
            case "ng": //nigeria
                return R.drawable.ng_circle;
            case "ni": //nicaragua
                return R.drawable.ni_circle;
            case "nl": //netherlands
                return R.drawable.nl_circle;
            case "no": //norway
                return R.drawable.no_circle;
            case "np": //nepal
                return R.drawable.np_circle;
            case "nr": //nauru
                return R.drawable.nr_circle;
            case "nu": //niue
                return R.drawable.nu_circle;
            case "nz": //new zealand
                return R.drawable.nz_circle;
            case "om": //oman
                return R.drawable.om_circle;
            case "pa": //panama
                return R.drawable.pa_circle;
            case "pe": //peru
                return R.drawable.pe_circle;
            case "pf": //french polynesia
                return R.drawable.pf_circle;
            case "pg": //papua new guinea
                return R.drawable.pg_circle;
            case "ph": //philippines
                return R.drawable.ph_circle;
            case "pk": //pakistan
                return R.drawable.pk_circle;
            case "pl": //poland
                return R.drawable.pl_circle;
            case "pm": //saint pierre and miquelon
                return R.drawable.pm_circle;
            case "pn": //pitcairn
                return R.drawable.pn_circle;
            case "pr": //puerto rico
                return R.drawable.pr_circle;
            case "ps": //palestine
                return R.drawable.ps_circle;
            case "pt": //portugal
                return R.drawable.pt_circle;
            case "pw": //palau
                return R.drawable.pw_circle;
            case "py": //paraguay
                return R.drawable.py_circle;
            case "qa": //qatar
                return R.drawable.qa_circle;
            case "re": //la reunion
                return R.drawable.re_circle; // no exact flag found
            case "ro": //romania
                return R.drawable.ro_circle;
            case "rs": //serbia
                return R.drawable.rs_circle; // custom
            case "ru": //russian federation
                return R.drawable.ru_circle;
            case "rw": //rwanda
                return R.drawable.rw_circle;
            case "sa": //saudi arabia
                return R.drawable.sa_circle;
            case "sb": //solomon islands
                return R.drawable.sb_circle;
            case "sc": //seychelles
                return R.drawable.sc_circle;
            case "sd": //sudan
                return R.drawable.sd_circle;
            case "se": //sweden
                return R.drawable.se_circle;
            case "sg": //singapore
                return R.drawable.sg_circle;
            case "sh": //saint helena, ascension and tristan da cunha
                return R.drawable.sh_hl_circle; // custom
            case "si": //slovenia
                return R.drawable.si_circle;
            case "sk": //slovakia
                return R.drawable.sk_circle;
            case "sl": //sierra leone
                return R.drawable.sl_circle;
            case "sm": //san marino
                return R.drawable.sm_circle;
            case "sn": //senegal
                return R.drawable.sn_circle;
            case "so": //somalia
                return R.drawable.so_circle;
            case "sr": //suriname
                return R.drawable.sr_circle;
            case "ss": //south sudan
                return R.drawable.ss_circle;
            case "st": //sao tome and principe
                return R.drawable.st_circle;
            case "sv": //el salvador
                return R.drawable.sv_circle;
            case "sx": //sint maarten
                return R.drawable.sx_circle;
            case "sy": //syrian arab republic
                return R.drawable.sy_circle;
            case "sz": //swaziland
                return R.drawable.sz_circle;
            case "tc": //turks & caicos islands
                return R.drawable.tc_circle;
            case "td": //chad
                return R.drawable.td_circle;
            case "tg": //togo
                return R.drawable.tg_circle;
            case "th": //thailand
                return R.drawable.th_circle;
            case "tj": //tajikistan
                return R.drawable.tj_circle;
            case "tk": //tokelau
                return R.drawable.tk_circle; // custom
            case "tl": //timor-leste
                return R.drawable.tl_circle;
            case "tm": //turkmenistan
                return R.drawable.tm_circle;
            case "tn": //tunisia
                return R.drawable.tn_circle;
            case "to": //tonga
                return R.drawable.to_circle;
            case "tr": //turkey
                return R.drawable.tr_circle;
            case "tt": //trinidad & tobago
                return R.drawable.tt_circle;
            case "tv": //tuvalu
                return R.drawable.tv_circle;
            case "tw": //taiwan, province of china
                return R.drawable.tw_circle;
            case "tz": //tanzania, united republic of
                return R.drawable.tz_circle;
            case "ua": //ukraine
                return R.drawable.ua_circle;
            case "ug": //uganda
                return R.drawable.ug_circle;
            case "us": //united states
                return R.drawable.us_circle;
            case "uy": //uruguay
                return R.drawable.uy_circle;
            case "uz": //uzbekistan
                return R.drawable.uz_circle;
            case "va": //holy see (vatican city state)
                return R.drawable.va_circle;
            case "vc": //st vincent & the grenadines
                return R.drawable.vc_circle;
            case "ve": //venezuela, bolivarian republic of
                return R.drawable.ve_circle;
            case "vg": //british virgin islands
                return R.drawable.vg_circle;
            case "vi": //us virgin islands
                return R.drawable.vi_circle;
            case "vn": //vietnam
                return R.drawable.vn_circle;
            case "vu": //vanuatu
                return R.drawable.vu_circle;
            case "wf": //wallis and futuna
                return R.drawable.wf_circle;
            case "ws": //samoa
                return R.drawable.ws_circle;
            case "xk": //kosovo
                return R.drawable.xk_circle;
            case "ye": //yemen
                return R.drawable.ye_circle;
            case "yt": //mayotte
                return R.drawable.yt_circle; // no exact flag found
            case "za": //south africa
                return R.drawable.za_circle;
            case "zm": //zambia
                return R.drawable.zm_circle;
            case "zw": //zimbabwe
                return R.drawable.zw_circle;
            default:
                return R.drawable.flag_transparent;
        }
    }


    static int getRoundedRectangleFlagMasterResID(CCPCountry CCPCountry) {
        switch (CCPCountry.getNameCode().toLowerCase()) {
            //this should be sorted based on country name code.
            case "ad": //andorra
                return R.drawable.ad_rounded_rectangle;
            case "ae": //united arab emirates
                return R.drawable.ae_rounded_rectangle;
            case "af": //afghanistan
                return R.drawable.af_rounded_rectangle;
            case "ag": //antigua & barbuda
                return R.drawable.ag_rounded_rectangle;
            case "ai": //anguilla // Caribbean Islands
                return R.drawable.ai_rounded_rectangle;
            case "al": //albania
                return R.drawable.al_rounded_rectangle;
            case "am": //armenia
                return R.drawable.am_rounded_rectangle;
            case "ao": //angola
                return R.drawable.ao_rounded_rectangle;
            case "aq": //antarctica // custom
                return R.drawable.aq_rounded_rectangle;
            case "ar": //argentina
                return R.drawable.ar_rounded_rectangle;
            case "as": //American Samoa
                return R.drawable.as_rounded_rectangle;
            case "at": //austria
                return R.drawable.at_rounded_rectangle;
            case "au": //australia
                return R.drawable.au_rounded_rectangle;
            case "aw": //aruba
                return R.drawable.aw_rounded_rectangle;
            case "ax": //alan islands
                return R.drawable.ax_rounded_rectangle;
            case "az": //azerbaijan
                return R.drawable.az_rounded_rectangle;
            case "ba": //bosnia and herzegovina
                return R.drawable.ba_rounded_rectangle;
            case "bb": //barbados
                return R.drawable.bb_rounded_rectangle;
            case "bd": //bangladesh
                return R.drawable.bd_rounded_rectangle;
            case "be": //belgium
                return R.drawable.be_rounded_rectangle;
            case "bf": //burkina faso
                return R.drawable.bf_rounded_rectangle;
            case "bg": //bulgaria
                return R.drawable.bg_rounded_rectangle;
            case "bh": //bahrain
                return R.drawable.bh_rounded_rectangle;
            case "bi": //burundi
                return R.drawable.bi_rounded_rectangle;
            case "bj": //benin
                return R.drawable.bj_rounded_rectangle;
            case "bl": //saint barthélemy
                return R.drawable.bl_rounded_rectangle;// custom
            case "bm": //bermuda
                return R.drawable.bm_rounded_rectangle;
            case "bn": //brunei darussalam // custom
                return R.drawable.bn_rounded_rectangle;
            case "bo": //bolivia, plurinational state of
                return R.drawable.bo_rounded_rectangle;
            case "br": //brazil
                return R.drawable.br_rounded_rectangle;
            case "bs": //bahamas
                return R.drawable.bs_rounded_rectangle;
            case "bt": //bhutan
                return R.drawable.bt_rounded_rectangle;
            case "bw": //botswana
                return R.drawable.bw_rounded_rectangle;
            case "by": //belarus
                return R.drawable.by_rounded_rectangle;
            case "bz": //belize
                return R.drawable.bz_rounded_rectangle;
            case "ca": //canada
                return R.drawable.ca_rounded_rectangle;
            case "cc": //cocos (keeling) islands
                return R.drawable.cc_rounded_rectangle;// custom
            case "cd": //congo, the democratic republic of the
                return R.drawable.cd_rounded_rectangle;
            case "cf": //central african republic
                return R.drawable.cf_rounded_rectangle;
            case "cg": //congo
                return R.drawable.cg_rounded_rectangle;
            case "ch": //switzerland
                return R.drawable.ch_rounded_rectangle;
            case "ci": //côte d\'ivoire
                return R.drawable.ci_rounded_rectangle;
            case "ck": //cook islands
                return R.drawable.ck_rounded_rectangle;
            case "cl": //chile
                return R.drawable.cl_rounded_rectangle;
            case "cm": //cameroon
                return R.drawable.cm_rounded_rectangle;
            case "cn": //china
                return R.drawable.cn_rounded_rectangle;
            case "co": //colombia
                return R.drawable.co_rounded_rectangle;
            case "cr": //costa rica
                return R.drawable.cr_rounded_rectangle;
            case "cu": //cuba
                return R.drawable.cu_rounded_rectangle;
            case "cv": //cape verde
                return R.drawable.cv_rounded_rectangle;
            case "cw": //curaçao
                return R.drawable.cw_rounded_rectangle;
            case "cx": //christmas island
                return R.drawable.cx_rounded_rectangle;
            case "cy": //cyprus
                return R.drawable.cy_rounded_rectangle;
            case "cz": //czech republic
                return R.drawable.cz_rounded_rectangle;
            case "de": //germany
                return R.drawable.de_rounded_rectangle;
            case "dj": //djibouti
                return R.drawable.dj_rounded_rectangle;
            case "dk": //denmark
                return R.drawable.dk_rounded_rectangle;
            case "dm": //dominica
                return R.drawable.dm_rounded_rectangle;
            case "do": //dominican republic
                return R.drawable.do_rounded_rectangle;
            case "dz": //algeria
                return R.drawable.dz_rounded_rectangle;
            case "ec": //ecuador
                return R.drawable.ec_rounded_rectangle;
            case "ee": //estonia
                return R.drawable.ee_rounded_rectangle;
            case "eg": //egypt
                return R.drawable.eg_rounded_rectangle;
            case "er": //eritrea
                return R.drawable.er_rounded_rectangle;
            case "es": //spain
                return R.drawable.es_rounded_rectangle;
            case "et": //ethiopia
                return R.drawable.et_rounded_rectangle;
            case "fi": //finland
                return R.drawable.fi_rounded_rectangle;
            case "fj": //fiji
                return R.drawable.fj_rounded_rectangle;
            case "fk": //falkland islands (malvinas)
                return R.drawable.fk_rounded_rectangle;
            case "fm": //micronesia, federated states of
                return R.drawable.fm_rounded_rectangle;
            case "fo": //faroe islands
                return R.drawable.fo_rounded_rectangle;
            case "fr": //france
                return R.drawable.fr_rounded_rectangle;
            case "ga": //gabon
                return R.drawable.ga_rounded_rectangle;
            case "gb": //united kingdom
                return R.drawable.gb_rounded_rectangle;
            case "gd": //grenada
                return R.drawable.gd_rounded_rectangle;
            case "ge": //georgia
                return R.drawable.ge_rounded_rectangle;
            case "gf": //guyane
                return R.drawable.gf_rounded_rectangle;
            case "gg": //Guernsey
                return R.drawable.gg_rounded_rectangle;
            case "gh": //ghana
                return R.drawable.gh_rounded_rectangle;
            case "gi": //gibraltar
                return R.drawable.gi_rounded_rectangle;
            case "gl": //greenland
                return R.drawable.gl_rounded_rectangle;
            case "gm": //gambia
                return R.drawable.gm_rounded_rectangle;
            case "gn": //guinea
                return R.drawable.gn_rounded_rectangle;
            case "gp": //guadeloupe
                return R.drawable.gp_rounded_rectangle;
            case "gq": //equatorial guinea
                return R.drawable.gq_rounded_rectangle;
            case "gr": //greece
                return R.drawable.gr_rounded_rectangle;
            case "gt": //guatemala
                return R.drawable.gt_rounded_rectangle;
            case "gu": //Guam
                return R.drawable.gu_rounded_rectangle;
            case "gw": //guinea-bissau
                return R.drawable.gw_rounded_rectangle;
            case "gy": //guyana
                return R.drawable.gy_rounded_rectangle;
            case "hk": //hong kong
                return R.drawable.hk_rounded_rectangle;
            case "hn": //honduras
                return R.drawable.hn_rounded_rectangle;
            case "hr": //croatia
                return R.drawable.hr_rounded_rectangle;
            case "ht": //haiti
                return R.drawable.ht_rounded_rectangle;
            case "hu": //hungary
                return R.drawable.hu_rounded_rectangle;
            case "id": //indonesia
                return R.drawable.id_rounded_rectangle;
            case "ie": //ireland
                return R.drawable.ie_rounded_rectangle;
            case "il": //israel
                return R.drawable.il_rounded_rectangle;
            case "im": //isle of man
                return R.drawable.im_rounded_rectangle; // custom
            case "is": //Iceland
                return R.drawable.is_rounded_rectangle;
            case "in": //india
                return R.drawable.in_rounded_rectangle;
            case "io": //British indian ocean territory
                return R.drawable.io_rounded_rectangle;
            case "iq": //iraq
                return R.drawable.iq_rounded_rectangle;
            case "ir": //iran, islamic republic of
                return R.drawable.ir_rounded_rectangle;
            case "it": //italy
                return R.drawable.it_rounded_rectangle;
            case "je": //Jersey
                return R.drawable.je_rounded_rectangle;
            case "jm": //jamaica
                return R.drawable.jm_rounded_rectangle;
            case "jo": //jordan
                return R.drawable.jo_rounded_rectangle;
            case "jp": //japan
                return R.drawable.jp_rounded_rectangle;
            case "ke": //kenya
                return R.drawable.ke_rounded_rectangle;
            case "kg": //kyrgyzstan
                return R.drawable.kg_rounded_rectangle;
            case "kh": //cambodia
                return R.drawable.kh_rounded_rectangle;
            case "ki": //kiribati
                return R.drawable.ki_rounded_rectangle;
            case "km": //comoros
                return R.drawable.km_rounded_rectangle;
            case "kn": //st kitts & nevis
                return R.drawable.kn_rounded_rectangle;
            case "kp": //north korea
                return R.drawable.kp_rounded_rectangle;
            case "kr": //south korea
                return R.drawable.kr_rounded_rectangle;
            case "kw": //kuwait
                return R.drawable.kw_rounded_rectangle;
            case "ky": //Cayman_Islands
                return R.drawable.ky_rounded_rectangle;
            case "kz": //kazakhstan
                return R.drawable.kz_rounded_rectangle;
            case "la": //lao people\'s democratic republic
                return R.drawable.la_rounded_rectangle;
            case "lb": //lebanon
                return R.drawable.lb_rounded_rectangle;
            case "lc": //st lucia
                return R.drawable.lc_rounded_rectangle;
            case "li": //liechtenstein
                return R.drawable.li_rounded_rectangle;
            case "lk": //sri lanka
                return R.drawable.lk_rounded_rectangle;
            case "lr": //liberia
                return R.drawable.lr_rounded_rectangle;
            case "ls": //lesotho
                return R.drawable.ls_rounded_rectangle;
            case "lt": //lithuania
                return R.drawable.lt_rounded_rectangle;
            case "lu": //luxembourg
                return R.drawable.lu_rounded_rectangle;
            case "lv": //latvia
                return R.drawable.lv_rounded_rectangle;
            case "ly": //libya
                return R.drawable.ly_rounded_rectangle;
            case "ma": //morocco
                return R.drawable.ma_rounded_rectangle;
            case "mc": //monaco
                return R.drawable.mc_rounded_rectangle;
            case "md": //moldova, republic of
                return R.drawable.md_rounded_rectangle;
            case "me": //montenegro
                return R.drawable.me_rounded_rectangle;// custom
            case "mf":
                return R.drawable.mf_rounded_rectangle;
            case "mg": //madagascar
                return R.drawable.mg_rounded_rectangle;
            case "mh": //marshall islands
                return R.drawable.mh_rounded_rectangle;
            case "mk": //macedonia, the former yugoslav republic of
                return R.drawable.mk_rounded_rectangle;
            case "ml": //mali
                return R.drawable.ml_rounded_rectangle;
            case "mm": //myanmar
                return R.drawable.mm_rounded_rectangle;
            case "mn": //mongolia
                return R.drawable.mn_rounded_rectangle;
            case "mo": //macao
                return R.drawable.mo_rounded_rectangle;
            case "mp": // Northern mariana islands
                return R.drawable.mp_rounded_rectangle;
            case "mq": //martinique
                return R.drawable.mq_rounded_rectangle;
            case "mr": //mauritania
                return R.drawable.mr_rounded_rectangle;
            case "ms": //montserrat
                return R.drawable.mq_rounded_rectangle;
            case "mt": //malta
                return R.drawable.mt_rounded_rectangle;
            case "mu": //mauritius
                return R.drawable.mu_rounded_rectangle;
            case "mv": //maldives
                return R.drawable.mv_rounded_rectangle;
            case "mw": //malawi
                return R.drawable.mw_rounded_rectangle;
            case "mx": //mexico
                return R.drawable.mx_rounded_rectangle;
            case "my": //malaysia
                return R.drawable.my_rounded_rectangle;
            case "mz": //mozambique
                return R.drawable.mz_rounded_rectangle;
            case "na": //namibia
                return R.drawable.na_rounded_rectangle;
            case "nc": //new caledonia
                return R.drawable.nc_rounded_rectangle;// custom
            case "ne": //niger
                return R.drawable.ne_rounded_rectangle;
            case "nf": //Norfolk
                return R.drawable.nf_rounded_rectangle;
            case "ng": //nigeria
                return R.drawable.ng_rounded_rectangle;
            case "ni": //nicaragua
                return R.drawable.ni_rounded_rectangle;
            case "nl": //netherlands
                return R.drawable.nl_rounded_rectangle;
            case "no": //norway
                return R.drawable.no_rounded_rectangle;
            case "np": //nepal
                return R.drawable.np_rounded_rectangle;
            case "nr": //nauru
                return R.drawable.nr_rounded_rectangle;
            case "nu": //niue
                return R.drawable.nu_rounded_rectangle;
            case "nz": //new zealand
                return R.drawable.nz_rounded_rectangle;
            case "om": //oman
                return R.drawable.om_rounded_rectangle;
            case "pa": //panama
                return R.drawable.pa_rounded_rectangle;
            case "pe": //peru
                return R.drawable.pe_rounded_rectangle;
            case "pf": //french polynesia
                return R.drawable.pf_rounded_rectangle;
            case "pg": //papua new guinea
                return R.drawable.pg_rounded_rectangle;
            case "ph": //philippines
                return R.drawable.ph_rounded_rectangle;
            case "pk": //pakistan
                return R.drawable.pk_rounded_rectangle;
            case "pl": //poland
                return R.drawable.pl_rounded_rectangle;
            case "pm": //saint pierre and miquelon
                return R.drawable.pm_rounded_rectangle;
            case "pn": //pitcairn
                return R.drawable.pn_rounded_rectangle;
            case "pr": //puerto rico
                return R.drawable.pr_rounded_rectangle;
            case "ps": //palestine
                return R.drawable.ps_rounded_rectangle;
            case "pt": //portugal
                return R.drawable.pt_rounded_rectangle;
            case "pw": //palau
                return R.drawable.pw_rounded_rectangle;
            case "py": //paraguay
                return R.drawable.py_rounded_rectangle;
            case "qa": //qatar
                return R.drawable.qa_rounded_rectangle;
            case "re": //la reunion
                return R.drawable.re_rounded_rectangle; // no exact flag found
            case "ro": //romania
                return R.drawable.ro_rounded_rectangle;
            case "rs": //serbia
                return R.drawable.rs_rounded_rectangle; // custom
            case "ru": //russian federation
                return R.drawable.ru_rounded_rectangle;
            case "rw": //rwanda
                return R.drawable.rw_rounded_rectangle;
            case "sa": //saudi arabia
                return R.drawable.sa_rounded_rectangle;
            case "sb": //solomon islands
                return R.drawable.sb_rounded_rectangle;
            case "sc": //seychelles
                return R.drawable.sc_rounded_rectangle;
            case "sd": //sudan
                return R.drawable.sd_rounded_rectangle;
            case "se": //sweden
                return R.drawable.se_rounded_rectangle;
            case "sg": //singapore
                return R.drawable.sg_rounded_rectangle;
            case "sh": //saint helena, ascension and tristan da cunha
                return R.drawable.sh_rounded_rectangle; // custom
            case "si": //slovenia
                return R.drawable.si_rounded_rectangle;
            case "sk": //slovakia
                return R.drawable.sk_rounded_rectangle;
            case "sl": //sierra leone
                return R.drawable.sl_rounded_rectangle;
            case "sm": //san marino
                return R.drawable.sm_rounded_rectangle;
            case "sn": //senegal
                return R.drawable.sn_rounded_rectangle;
            case "so": //somalia
                return R.drawable.so_rounded_rectangle;
            case "sr": //suriname
                return R.drawable.sr_rounded_rectangle;
            case "ss": //south sudan
                return R.drawable.ss_rounded_rectangle;
            case "st": //sao tome and principe
                return R.drawable.st_rounded_rectangle;
            case "sv": //el salvador
                return R.drawable.sv_rounded_rectangle;
            case "sx": //sint maarten
                return R.drawable.sx_rounded_rectangle;
            case "sy": //syrian arab republic
                return R.drawable.sy_rounded_rectangle;
            case "sz": //swaziland
                return R.drawable.sz_rounded_rectangle;
            case "tc": //turks & caicos islands
                return R.drawable.tc_rounded_rectangle;
            case "td": //chad
                return R.drawable.td_rounded_rectangle;
            case "tg": //togo
                return R.drawable.tg_rounded_rectangle;
            case "th": //thailand
                return R.drawable.th_rounded_rectangle;
            case "tj": //tajikistan
                return R.drawable.tj_rounded_rectangle;
            case "tk": //tokelau
                return R.drawable.tk_rounded_rectangle; // custom
            case "tl": //timor-leste
                return R.drawable.tl_rounded_rectangle;
            case "tm": //turkmenistan
                return R.drawable.tm_rounded_rectangle;
            case "tn": //tunisia
                return R.drawable.tn_rounded_rectangle;
            case "to": //tonga
                return R.drawable.to_rounded_rectangle;
            case "tr": //turkey
                return R.drawable.tr_rounded_rectangle;
            case "tt": //trinidad & tobago
                return R.drawable.tt_rounded_rectangle;
            case "tv": //tuvalu
                return R.drawable.tv_rounded_rectangle;
            case "tw": //taiwan, province of china
                return R.drawable.tw_rounded_rectangle;
            case "tz": //tanzania, united republic of
                return R.drawable.tz_rounded_rectangle;
            case "ua": //ukraine
                return R.drawable.ua_rounded_rectangle;
            case "ug": //uganda
                return R.drawable.ug_rounded_rectangle;
            case "us": //united states
                return R.drawable.us_rounded_rectangle;
            case "uy": //uruguay
                return R.drawable.uy_rounded_rectangle;
            case "uz": //uzbekistan
                return R.drawable.uz_rounded_rectangle;
            case "va": //holy see (vatican city state)
                return R.drawable.va_rounded_rectangle;
            case "vc": //st vincent & the grenadines
                return R.drawable.vc_rounded_rectangle;
            case "ve": //venezuela, bolivarian republic of
                return R.drawable.ve_rounded_rectangle;
            case "vg": //british virgin islands
                return R.drawable.vg_rounded_rectangle;
            case "vi": //us virgin islands
                return R.drawable.vi_rounded_rectangle;
            case "vn": //vietnam
                return R.drawable.vn_rounded_rectangle;
            case "vu": //vanuatu
                return R.drawable.vu_rounded_rectangle;
            case "wf": //wallis and futuna
                return R.drawable.wf_rounded_rectangle;
            case "ws": //samoa
                return R.drawable.ws_rounded_rectangle;
            case "xk": //kosovo
                return R.drawable.xk_rounded_rectangle;
            case "ye": //yemen
                return R.drawable.ye_rounded_rectangle;
            case "yt": //mayotte
                return R.drawable.yt_rounded_rectangle; // no exact flag found
            case "za": //south africa
                return R.drawable.za_rounded_rectangle;
            case "zm": //zambia
                return R.drawable.zm_rounded_rectangle;
            case "zw": //zimbabwe
                return R.drawable.zw_rounded_rectangle;
            default:
                return R.drawable.flag_transparent;
        }
    }


    /**
     * Returns image res based on country name code
     *
     * @param CCPCountry
     * @return
     */
    static String getFlagEmoji(CCPCountry CCPCountry) {
        switch (CCPCountry.getNameCode().toLowerCase()) {
            //this should be sorted based on country name code.
            case "ad":
                return "🇦🇩";
            case "ae":
                return "🇦🇪";
            case "af":
                return "🇦🇫";
            case "ag":
                return "🇦🇬";
            case "ai":
                return "🇦🇮";
            case "al":
                return "🇦🇱";
            case "am":
                return "🇦🇲";
            case "ao":
                return "🇦🇴";
            case "aq":
                return "🇦🇶";
            case "ar":
                return "🇦🇷";
            case "as":
                return "🇦🇸";
            case "at":
                return "🇦🇹";
            case "au":
                return "🇦🇺";
            case "aw":
                return "🇦🇼";
            case "ax":
                return "🇦🇽";
            case "az":
                return "🇦🇿";
            case "ba":
                return "🇧🇦";
            case "bb":
                return "🇧🇧";
            case "bd":
                return "🇧🇩";
            case "be":
                return "🇧🇪";
            case "bf":
                return "🇧🇫";
            case "bg":
                return "🇧🇬";
            case "bh":
                return "🇧🇭";
            case "bi":
                return "🇧🇮";
            case "bj":
                return "🇧🇯";
            case "bl":
                return "🇧🇱";
            case "bm":
                return "🇧🇲";
            case "bn":
                return "🇧🇳";
            case "bo":
                return "🇧🇴";
            case "bq":
                return "🇧🇶";
            case "br":
                return "🇧🇷";
            case "bs":
                return "🇧🇸";
            case "bt":
                return "🇧🇹";
            case "bv":
                return "🇧🇻";
            case "bw":
                return "🇧🇼";
            case "by":
                return "🇧🇾";
            case "bz":
                return "🇧🇿";
            case "ca":
                return "🇨🇦";
            case "cc":
                return "🇨🇨";
            case "cd":
                return "🇨🇩";
            case "cf":
                return "🇨🇫";
            case "cg":
                return "🇨🇬";
            case "ch":
                return "🇨🇭";
            case "ci":
                return "🇨🇮";
            case "ck":
                return "🇨🇰";
            case "cl":
                return "🇨🇱";
            case "cm":
                return "🇨🇲";
            case "cn":
                return "🇨🇳";
            case "co":
                return "🇨🇴";
            case "cr":
                return "🇨🇷";
            case "cu":
                return "🇨🇺";
            case "cv":
                return "🇨🇻";
            case "cw":
                return "🇨🇼";
            case "cx":
                return "🇨🇽";
            case "cy":
                return "🇨🇾";
            case "cz":
                return "🇨🇿";
            case "de":
                return "🇩🇪";
            case "dj":
                return "🇩🇯";
            case "dk":
                return "🇩🇰";
            case "dm":
                return "🇩🇲";
            case "do":
                return "🇩🇴";
            case "dz":
                return "🇩🇿";
            case "ec":
                return "🇪🇨";
            case "ee":
                return "🇪🇪";
            case "eg":
                return "🇪🇬";
            case "eh":
                return "🇪🇭";
            case "er":
                return "🇪🇷";
            case "es":
                return "🇪🇸";
            case "et":
                return "🇪🇹";
            case "fi":
                return "🇫🇮";
            case "fj":
                return "🇫🇯";
            case "fk":
                return "🇫🇰";
            case "fm":
                return "🇫🇲";
            case "fo":
                return "🇫🇴";
            case "fr":
                return "🇫🇷";
            case "ga":
                return "🇬🇦";
            case "gb":
                return "🇬🇧";
            case "gd":
                return "🇬🇩";
            case "ge":
                return "🇬🇪";
            case "gf":
                return "🇬🇫";
            case "gg":
                return "🇬🇬";
            case "gh":
                return "🇬🇭";
            case "gi":
                return "🇬🇮";
            case "gl":
                return "🇬🇱";
            case "gm":
                return "🇬🇲";
            case "gn":
                return "🇬🇳";
            case "gp":
                return "🇬🇵";
            case "gq":
                return "🇬🇶";
            case "gr":
                return "🇬🇷";
            case "gs":
                return "🇬🇸";
            case "gt":
                return "🇬🇹";
            case "gu":
                return "🇬🇺";
            case "gw":
                return "🇬🇼";
            case "gy":
                return "🇬🇾";
            case "hk":
                return "🇭🇰";
            case "hm":
                return "🇭🇲";
            case "hn":
                return "🇭🇳";
            case "hr":
                return "🇭🇷";
            case "ht":
                return "🇭🇹";
            case "hu":
                return "🇭🇺";
            case "id":
                return "🇮🇩";
            case "ie":
                return "🇮🇪";
            case "il":
                return "🇮🇱";
            case "im":
                return "🇮🇲";
            case "in":
                return "🇮🇳";
            case "io":
                return "🇮🇴";
            case "iq":
                return "🇮🇶";
            case "ir":
                return "🇮🇷";
            case "is":
                return "🇮🇸";
            case "it":
                return "🇮🇹";
            case "je":
                return "🇯🇪";
            case "jm":
                return "🇯🇲";
            case "jo":
                return "🇯🇴";
            case "jp":
                return "🇯🇵";
            case "ke":
                return "🇰🇪";
            case "kg":
                return "🇰🇬";
            case "kh":
                return "🇰🇭";
            case "ki":
                return "🇰🇮";
            case "km":
                return "🇰🇲";
            case "kn":
                return "🇰🇳";
            case "kp":
                return "🇰🇵";
            case "kr":
                return "🇰🇷";
            case "kw":
                return "🇰🇼";
            case "ky":
                return "🇰🇾";
            case "kz":
                return "🇰🇿";
            case "la":
                return "🇱🇦";
            case "lb":
                return "🇱🇧";
            case "lc":
                return "🇱🇨";
            case "li":
                return "🇱🇮";
            case "lk":
                return "🇱🇰";
            case "lr":
                return "🇱🇷";
            case "ls":
                return "🇱🇸";
            case "lt":
                return "🇱🇹";
            case "lu":
                return "🇱🇺";
            case "lv":
                return "🇱🇻";
            case "ly":
                return "🇱🇾";
            case "ma":
                return "🇲🇦";
            case "mc":
                return "🇲🇨";
            case "md":
                return "🇲🇩";
            case "me":
                return "🇲🇪";
            case "mf":
                return "🇲🇫";
            case "mg":
                return "🇲🇬";
            case "mh":
                return "🇲🇭";
            case "mk":
                return "🇲🇰";
            case "ml":
                return "🇲🇱";
            case "mm":
                return "🇲🇲";
            case "mn":
                return "🇲🇳";
            case "mo":
                return "🇲🇴";
            case "mp":
                return "🇲🇵";
            case "mq":
                return "🇲🇶";
            case "mr":
                return "🇲🇷";
            case "ms":
                return "🇲🇸";
            case "mt":
                return "🇲🇹";
            case "mu":
                return "🇲🇺";
            case "mv":
                return "🇲🇻";
            case "mw":
                return "🇲🇼";
            case "mx":
                return "🇲🇽";
            case "my":
                return "🇲🇾";
            case "mz":
                return "🇲🇿";
            case "na":
                return "🇳🇦";
            case "nc":
                return "🇳🇨";
            case "ne":
                return "🇳🇪";
            case "nf":
                return "🇳🇫";
            case "ng":
                return "🇳🇬";
            case "ni":
                return "🇳🇮";
            case "nl":
                return "🇳🇱";
            case "no":
                return "🇳🇴";
            case "np":
                return "🇳🇵";
            case "nr":
                return "🇳🇷";
            case "nu":
                return "🇳🇺";
            case "nz":
                return "🇳🇿";
            case "om":
                return "🇴🇲";
            case "pa":
                return "🇵🇦";
            case "pe":
                return "🇵🇪";
            case "pf":
                return "🇵🇫";
            case "pg":
                return "🇵🇬";
            case "ph":
                return "🇵🇭";
            case "pk":
                return "🇵🇰";
            case "pl":
                return "🇵🇱";
            case "pm":
                return "🇵🇲";
            case "pn":
                return "🇵🇳";
            case "pr":
                return "🇵🇷";
            case "ps":
                return "🇵🇸";
            case "pt":
                return "🇵🇹";
            case "pw":
                return "🇵🇼";
            case "py":
                return "🇵🇾";
            case "qa":
                return "🇶🇦";
            case "re":
                return "🇷🇪";
            case "ro":
                return "🇷🇴";
            case "rs":
                return "🇷🇸";
            case "ru":
                return "🇷🇺";
            case "rw":
                return "🇷🇼";
            case "sa":
                return "🇸🇦";
            case "sb":
                return "🇸🇧";
            case "sc":
                return "🇸🇨";
            case "sd":
                return "🇸🇩";
            case "se":
                return "🇸🇪";
            case "sg":
                return "🇸🇬";
            case "sh":
                return "🇸🇭";
            case "si":
                return "🇸🇮";
            case "sj":
                return "🇸🇯";
            case "sk":
                return "🇸🇰";
            case "sl":
                return "🇸🇱";
            case "sm":
                return "🇸🇲";
            case "sn":
                return "🇸🇳";
            case "so":
                return "🇸🇴";
            case "sr":
                return "🇸🇷";
            case "ss":
                return "🇸🇸";
            case "st":
                return "🇸🇹";
            case "sv":
                return "🇸🇻";
            case "sx":
                return "🇸🇽";
            case "sy":
                return "🇸🇾";
            case "sz":
                return "🇸🇿";
            case "tc":
                return "🇹🇨";
            case "td":
                return "🇹🇩";
            case "tf":
                return "🇹🇫";
            case "tg":
                return "🇹🇬";
            case "th":
                return "🇹🇭";
            case "tj":
                return "🇹🇯";
            case "tk":
                return "🇹🇰";
            case "tl":
                return "🇹🇱";
            case "tm":
                return "🇹🇲";
            case "tn":
                return "🇹🇳";
            case "to":
                return "🇹🇴";
            case "tr":
                return "🇹🇷";
            case "tt":
                return "🇹🇹";
            case "tv":
                return "🇹🇻";
            case "tw":
                return "🇹🇼";
            case "tz":
                return "🇹🇿";
            case "ua":
                return "🇺🇦";
            case "ug":
                return "🇺🇬";
            case "um":
                return "🇺🇲";
            case "us":
                return "🇺🇸";
            case "uy":
                return "🇺🇾";
            case "uz":
                return "🇺🇿";
            case "va":
                return "🇻🇦";
            case "vc":
                return "🇻🇨";
            case "ve":
                return "🇻🇪";
            case "vg":
                return "🇻🇬";
            case "vi":
                return "🇻🇮";
            case "vn":
                return "🇻🇳";
            case "vu":
                return "🇻🇺";
            case "wf":
                return "🇼🇫";
            case "ws":
                return "🇼🇸";
            case "xk":
                return "🇽🇰";
            case "ye":
                return "🇾🇪";
            case "yt":
                return "🇾🇹";
            case "za":
                return "🇿🇦";
            case "zm":
                return "🇿🇲";
            case "zw":
                return "🇿🇼";
            default:
                return " ";
        }
    }

    /**
     * This will return all the countries. No preference is manages.
     * Anytime new country need to be added, add it
     *
     * @return
     */
    public static List<CCPCountry> getLibraryMasterCountryList(Context context, CountryCodePicker.Language language) {
        if (loadedLibraryMasterListLanguage == null || language != loadedLibraryMasterListLanguage || loadedLibraryMaterList == null || loadedLibraryMaterList.size() == 0) { //when it is required to load country in country list
            loadDataFromXML(context, language);
        }
        return loadedLibraryMaterList;
    }

    public static List<CCPCountry> getLibraryMasterCountriesEnglish() {
        List<CCPCountry> countries = new ArrayList<>();
        countries.add(new CCPCountry("ad", "376", "Andorra", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ae", "971", "United Arab Emirates (UAE)", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("af", "93", "Afghanistan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ag", "1", "Antigua and Barbuda", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ai", "1", "Anguilla", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("al", "355", "Albania", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("am", "374", "Armenia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ao", "244", "Angola", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("aq", "672", "Antarctica", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ar", "54", "Argentina", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("as", "1", "American Samoa", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("at", "43", "Austria", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("au", "61", "Australia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("aw", "297", "Aruba", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ax", "358", "Åland Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("az", "994", "Azerbaijan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ba", "387", "Bosnia And Herzegovina", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bb", "1", "Barbados", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bd", "880", "Bangladesh", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("be", "32", "Belgium", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bf", "226", "Burkina Faso", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bg", "359", "Bulgaria", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bh", "973", "Bahrain", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bi", "257", "Burundi", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bj", "229", "Benin", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bl", "590", "Saint Barthélemy", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bm", "1", "Bermuda", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bn", "673", "Brunei Darussalam", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bo", "591", "Bolivia, Plurinational State Of", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("br", "55", "Brazil", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bs", "1", "Bahamas", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bt", "975", "Bhutan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bw", "267", "Botswana", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("by", "375", "Belarus", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("bz", "501", "Belize", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ca", "1", "Canada", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cc", "61", "Cocos (keeling) Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cd", "243", "Congo, The Democratic Republic Of The", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cf", "236", "Central African Republic", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cg", "242", "Congo", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ch", "41", "Switzerland", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ci", "225", "Côte D'ivoire", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ck", "682", "Cook Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cl", "56", "Chile", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cm", "237", "Cameroon", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cn", "86", "China", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("co", "57", "Colombia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cr", "506", "Costa Rica", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cu", "53", "Cuba", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cv", "238", "Cape Verde", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cw", "599", "Curaçao", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cx", "61", "Christmas Island", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cy", "357", "Cyprus", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("cz", "420", "Czech Republic", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("de", "49", "Germany", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("dj", "253", "Djibouti", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("dk", "45", "Denmark", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("dm", "1", "Dominica", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("do", "1", "Dominican Republic", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("dz", "213", "Algeria", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ec", "593", "Ecuador", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ee", "372", "Estonia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("eg", "20", "Egypt", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("er", "291", "Eritrea", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("es", "34", "Spain", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("et", "251", "Ethiopia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("fi", "358", "Finland", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("fj", "679", "Fiji", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("fk", "500", "Falkland Islands (malvinas)", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("fm", "691", "Micronesia, Federated States Of", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("fo", "298", "Faroe Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("fr", "33", "France", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ga", "241", "Gabon", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gb", "44", "United Kingdom", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gd", "1", "Grenada", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ge", "995", "Georgia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gf", "594", "French Guyana", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gh", "233", "Ghana", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gi", "350", "Gibraltar", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gl", "299", "Greenland", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gm", "220", "Gambia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gn", "224", "Guinea", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gp", "450", "Guadeloupe", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gq", "240", "Equatorial Guinea", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gr", "30", "Greece", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gt", "502", "Guatemala", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gu", "1", "Guam", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gw", "245", "Guinea-bissau", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("gy", "592", "Guyana", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("hk", "852", "Hong Kong", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("hn", "504", "Honduras", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("hr", "385", "Croatia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ht", "509", "Haiti", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("hu", "36", "Hungary", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("id", "62", "Indonesia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ie", "353", "Ireland", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("il", "972", "Israel", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("im", "44", "Isle Of Man", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("is", "354", "Iceland", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("in", "91", "India", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("io", "246", "British Indian Ocean Territory", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("iq", "964", "Iraq", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ir", "98", "Iran, Islamic Republic Of", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("it", "39", "Italy", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("je", "44", "Jersey ", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("jm", "1", "Jamaica", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("jo", "962", "Jordan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("jp", "81", "Japan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ke", "254", "Kenya", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("kg", "996", "Kyrgyzstan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("kh", "855", "Cambodia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ki", "686", "Kiribati", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("km", "269", "Comoros", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("kn", "1", "Saint Kitts and Nevis", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("kp", "850", "North Korea", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("kr", "82", "South Korea", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("kw", "965", "Kuwait", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ky", "1", "Cayman Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("kz", "7", "Kazakhstan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("la", "856", "Lao People's Democratic Republic", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("lb", "961", "Lebanon", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("lc", "1", "Saint Lucia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("li", "423", "Liechtenstein", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("lk", "94", "Sri Lanka", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("lr", "231", "Liberia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ls", "266", "Lesotho", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("lt", "370", "Lithuania", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("lu", "352", "Luxembourg", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("lv", "371", "Latvia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ly", "218", "Libya", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ma", "212", "Morocco", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mc", "377", "Monaco", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("md", "373", "Moldova, Republic Of", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("me", "382", "Montenegro", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mf", "590", "Saint Martin", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mg", "261", "Madagascar", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mh", "692", "Marshall Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mk", "389", "Macedonia (FYROM)", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ml", "223", "Mali", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mm", "95", "Myanmar", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mn", "976", "Mongolia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mo", "853", "Macau", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mp", "1", "Northern Mariana Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mq", "596", "Martinique", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mr", "222", "Mauritania", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ms", "1", "Montserrat", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mt", "356", "Malta", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mu", "230", "Mauritius", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mv", "960", "Maldives", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mw", "265", "Malawi", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mx", "52", "Mexico", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("my", "60", "Malaysia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("mz", "258", "Mozambique", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("na", "264", "Namibia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("nc", "687", "New Caledonia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ne", "227", "Niger", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("nf", "672", "Norfolk Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ng", "234", "Nigeria", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ni", "505", "Nicaragua", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("nl", "31", "Netherlands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("no", "47", "Norway", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("np", "977", "Nepal", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("nr", "674", "Nauru", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("nu", "683", "Niue", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("nz", "64", "New Zealand", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("om", "968", "Oman", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pa", "507", "Panama", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pe", "51", "Peru", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pf", "689", "French Polynesia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pg", "675", "Papua New Guinea", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ph", "63", "Philippines", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pk", "92", "Pakistan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pl", "48", "Poland", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pm", "508", "Saint Pierre And Miquelon", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pn", "870", "Pitcairn Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pr", "1", "Puerto Rico", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ps", "970", "Palestine", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pt", "351", "Portugal", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("pw", "680", "Palau", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("py", "595", "Paraguay", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("qa", "974", "Qatar", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("re", "262", "Réunion", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ro", "40", "Romania", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("rs", "381", "Serbia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ru", "7", "Russian Federation", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("rw", "250", "Rwanda", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sa", "966", "Saudi Arabia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sb", "677", "Solomon Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sc", "248", "Seychelles", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sd", "249", "Sudan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("se", "46", "Sweden", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sg", "65", "Singapore", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sh", "290", "Saint Helena, Ascension And Tristan Da Cunha", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("si", "386", "Slovenia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sk", "421", "Slovakia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sl", "232", "Sierra Leone", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sm", "378", "San Marino", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sn", "221", "Senegal", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("so", "252", "Somalia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sr", "597", "Suriname", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ss", "211", "South Sudan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("st", "239", "Sao Tome And Principe", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sv", "503", "El Salvador", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sx", "1", "Sint Maarten", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sy", "963", "Syrian Arab Republic", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("sz", "268", "Swaziland", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tc", "1", "Turks and Caicos Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("td", "235", "Chad", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tg", "228", "Togo", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("th", "66", "Thailand", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tj", "992", "Tajikistan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tk", "690", "Tokelau", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tl", "670", "Timor-leste", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tm", "993", "Turkmenistan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tn", "216", "Tunisia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("to", "676", "Tonga", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tr", "90", "Turkey", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tt", "1", "Trinidad &amp; Tobago", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tv", "688", "Tuvalu", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tw", "886", "Taiwan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("tz", "255", "Tanzania, United Republic Of", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ua", "380", "Ukraine", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ug", "256", "Uganda", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("us", "1", "United States", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("uy", "598", "Uruguay", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("uz", "998", "Uzbekistan", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("va", "379", "Holy See (vatican City State)", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("vc", "1", "Saint Vincent &amp; The Grenadines", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ve", "58", "Venezuela, Bolivarian Republic Of", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("vg", "1", "British Virgin Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("vi", "1", "US Virgin Islands", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("vn", "84", "Vietnam", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("vu", "678", "Vanuatu", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("wf", "681", "Wallis And Futuna", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ws", "685", "Samoa", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("xk", "383", "Kosovo", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("ye", "967", "Yemen", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("yt", "262", "Mayotte", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("za", "27", "South Africa", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("zm", "260", "Zambia", DEFAULT_FLAG_RES));
        countries.add(new CCPCountry("zw", "263", "Zimbabwe", DEFAULT_FLAG_RES));
        return countries;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public int getFlagID() {
        if (flagResID == -99) {
            flagResID = getFlagMasterResID(this);
        }
        return flagResID;
    }

    public String getNameCode() {
        return nameCode;
    }

    public void setNameCode(String nameCode) {
        this.nameCode = nameCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void log() {
        try {
            Log.d(TAG, "Country->" + nameCode + ":" + phoneCode + ":" + name);
        } catch (NullPointerException ex) {
            Log.d(TAG, "Null");
        }
    }

    String logString() {
        return nameCode.toUpperCase(Locale.US) + " +" + phoneCode + "(" + name + ")";
    }

    /**
     * If country have query word in name or name code or phone code, this will return true.
     *
     * @param query
     * @return
     */
    boolean isEligibleForQuery(String query) {
        query = query.toLowerCase();
        return containsQueryWord("Name", getName(), query) ||
                containsQueryWord("NameCode", getNameCode(), query) ||
                containsQueryWord("PhoneCode", getPhoneCode(), query) ||
                containsQueryWord("EnglishName", getEnglishName(), query);
    }

    private boolean containsQueryWord(String fieldName, String fieldValue, String query) {
        try {
            if (fieldValue == null || query == null) {
                return false;
            } else {
                return fieldValue.toLowerCase(Locale.ROOT).contains(query);
            }
        } catch (Exception e) {
            Log.w("CCPCountry", fieldName + ":" + fieldValue +
                    " failed to execute toLowerCase(Locale.ROOT).contains(query) " +
                    "for query:" + query);
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull CCPCountry o) {
        return Collator.getInstance().compare(getName(), o.getName());
    }
}
