package com.bcad.application.web.rest.util;

import com.bcad.application.config.PropertiesUtils;
import org.apache.commons.configuration.PropertiesConfiguration;

public class MathUtil {

	PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
	
	public static Float getRoundOffValue(Float fValue) {
		Float fReturn = 0f;
		
		if (!fValue.equals(null) && fValue != null) {
			Float fModulus = fValue % 100;
			Float fRemainder = 100 - fModulus;
			if (!(fRemainder > 50)) {
				fReturn = (float) (Math.ceil(fValue / 100f) * 100);
			} else {
				fReturn = (float) (Math.floor(fValue / 100f) * 100);
			}
		}
		return fReturn;
	}
	
	public static void main(String args[])
	{
		getRoundOffValue(49f);
		getRoundOffValue(51f);
		getRoundOffValue(27f);
		getRoundOffValue(10f);
		getRoundOffValue(9f);
		getRoundOffValue(99f);
		getRoundOffValue(75f);
		
		getRoundOffValue(234557f);
		getRoundOffValue(457894f);
		getRoundOffValue(425313f);
		getRoundOffValue(789542f);
		getRoundOffValue(957481f);
		getRoundOffValue(123543f);
		getRoundOffValue(589650f);
	}

}
