package com.desirecodes.countrycodepicker;

import com.desirecodes.codepicker.CCPCountry;
import com.desirecodes.codepicker.CCPTalkBackTextProvider;

class CCPCustomTalkBackProvider implements CCPTalkBackTextProvider {
    @Override
    public String getTalkBackTextForCountry(CCPCountry country) {
        if (country != null) {
            return "Country code is +" + country.getPhoneCode();
        } else {
            return null;
        }
    }
}
