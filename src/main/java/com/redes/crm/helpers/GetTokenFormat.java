package com.redes.crm.helpers;

public class GetTokenFormat {
	public String cutToken(String token) {
		String cuttingParameter = "Authorization";
		
		int indexBearerName = token.indexOf(cuttingParameter);
		int index = token.indexOf(cuttingParameter) + cuttingParameter.length();
		
		if (indexBearerName != -1) {
			String tokenValue = token.substring(index).trim();
		    
		    return tokenValue;
		} else {
		    return "Not found";
		}
		
	}
}
