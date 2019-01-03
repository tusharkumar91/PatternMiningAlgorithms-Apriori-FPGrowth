package com.uofr.csc440.project.project1.datamodel;

/**
 * Enum class for converting each attributeValue type
 * present in adult dataset to an integral value.
 * This is done for efficient search during itemset
 * generation. Searching for integral values will 
 * be much faster than searching for integers which is 
 * how Enums are represented behind the scenes.
 * @author tusharkumar
 *
 */
public enum AdultDataSetItem {
	AGE_10_19("Age", "10-19", 1, 1),
	AGE_20_29("Age", "20-29", 1, 2),
	AGE_30_39("Age", "30-39", 1, 3),
	AGE_40_49("Age", "40-49", 1, 4),
	AGE_50_59("Age", "50-59", 1, 5),
	AGE_60_69("Age", "60-69", 1, 6),
	AGE_70_79("Age", "70-79", 1, 7),
	AGE_80_89("Age", "80-89", 1, 8),
	AGE_90_99("Age", "90-99", 1, 9),
	AGE_UNKNOWN("Age", "Unkown", 1, 10),
	WORKCLASS_PRIVATE("Workclass", "Private", 2, 11),
	WORKCLASS_SELF_EMP_NOT_INC("Workclass", "Self-emp-not-inc", 2, 12),
	WORKCLASS_SELF_EMP_INC("Workclass", "Self-emp-inc", 2, 13),
	WORKCLASS_FEDERAL_GOV("Workclass", "Federal-gov", 2, 14),
	WORKCLASS_LOCAL_GOV("Workclass", "Local-gov", 2, 15),
	WORKCLASS_STATE_GOV("Workclass", "State-gov", 2, 16),
	WORKCLASS_WITHOUT_PAY("Workclass", "Without-pay", 2, 17),
	WORKCLASS_NEVER_WORKED("Workclass", "Never-worked", 2, 18),
	WORKCLASS_UNKNOWN("Workclass", "Unknown", 2, 19),
	//Bachelors, Some-college, 11th, HS-grad, Prof-school, Assoc-acdm, Assoc-voc, 
	//9th, 7th-8th, 12th, Masters, 1st-4th, 10th, Doctorate, 5th-6th, Preschool.
	EDUCATION_BACHELORS("Education", "Bachelors", 3, 20),
	EDUCATION_COLLEGE("Education", "Some-College", 3, 21),
	EDUCATION_11("Education", "11th", 3, 22),
	EDUCATION_HS("Education", "HS-grad", 3, 23),
	EDUCATION_PROF_SCH("Education", "Prof-school", 3, 24),
	EDUCATION_ASSOC_ACDM("Education", "Assoc-acdm", 3, 25),
	EDUCATION_ASSOC_VOC("Education", "Assoc-voc", 3, 26),
	EDUCATION_9("Education", "9th", 3, 27),
	EDUCATION_7_8("Education", "7th-8th", 3, 28),
	EDUCATION_12("Education", "12th", 3, 29),
	EDUCATION_MS("Education", "Masters", 3, 30),
	EDUCATION_1_4("Education", "1st-4th", 3, 31),
	EDUCATION_10("Education", "10th", 3, 32),
	EDUCATION_DOCTORATE("Education", "Doctorate", 3, 33),
	EDUCATION_5_6("Education", "5th-6th", 3, 34),
	EDUCATION_PRESCH("Education", "Preschool", 3, 35),
	EDUCATION_UNKNOWN("Education", "Unknown", 3, 36),
	//1-4, 5-8, 9-12, 13-16
	EDUCATION_NUM_1_4("Education-num", "1-4", 4, 37),
	EDUCATION_NUM_5_8("Education-num", "5-8", 4, 38),
	EDUCATION_NUM_9_12("Education-num", "9-12", 4, 39),
	EDUCATION_NUM_13_16("Education-num", "13-16", 4, 40),
	EDUCATION_NUM_UNKNOWN("Education-num", "Unknown", 4, 41),
	//Married-civ-spouse, Divorced, Never-married, Separated, Widowed, Married-spouse-absent, Married-AF-spouse.
	MARITAL_STATUS_CIV_SPOUSE("Marital-status", "Married-civ-spouse", 5, 42),
	MARITAL_STATUS_DIVORCED("Marital-status", "Divorced", 5, 43),
	MARITAL_STATUS_NEVER_MARRIED("Marital-status", "Never-married", 5, 44),
	MARITAL_STATUS_SEPARATED("Marital-status", "Separated", 5, 45),
	MARITAL_STATUS_WIDOWED("Marital-status", "Widowed", 5, 46),
	MARITAL_STATUS_MARROED_SPOUSE_ABSENT("Marital-status", "Married-spouse-absent", 5, 47),
	MARITAL_STATUS_MARRIED_AF_SPOUSE("Marital-status", "Married-AF-spouse", 5, 48),
	MARITAL_STATUS_UNKNOWN("Marital-status", "Unknown", 5, 49),
	//Tech-support, Craft-repair, Other-service, Sales, Exec-managerial, Prof-specialty, Handlers-cleaners, Machine-op-inspct, 
	//Adm-clerical, Farming-fishing, Transport-moving, Priv-house-serv, Protective-serv, Armed-Forces
	OCCUPATION_TECH_SUP("Occupation", "Tech-support", 6, 50),
	OCCUPATION_CRAFT_REPAIR("Occupation", "Craft-repair", 6, 51),
	OCCUPATION_OTHER("Occupation", "Other-service", 6, 52),
	OCCUPATION_SALES("Occupation", "Sales", 6, 53),
	OCCUPATION_EXEC_MANAGER("Occupation", "Exec-managerial", 6, 54),
	OCCUPATION_PROF_SPECIALITY("Occupation", "Prof-specialty", 6, 55),
	OCCUPATION_HANDLERS_CLEANERS("Occupation", "Handlers-cleaners", 6, 56),
	OCCUPATION_MACHINE_OP_INSPCT("Occupation", "Machine-op-inspct", 6, 57),
	OCCUPATION_ADM_CLERICAL("Occupation", "Adm-clerical", 6, 58),
	OCCUPATION_FARMING_FISHING("Occupation", "Farming-fishing", 6, 59),
	OCCUPATION_TRANSPORT_MOVING("Occupation", "Transport-moving", 6, 60),
	OCCUPATION_PRIV_HOUSE_SERV("Occupation", "Priv-house-serv", 6, 61),
	OCCUPATION_PROTECTIVE_SERV("Occupation", "Protective-serv", 6, 62),
	OCCUPATION_ARMED_FORCES("Occupation", "Armed-Forces", 6, 63),
	OCCUPATION_UNKNOWN("Occupation", "Unknown", 6, 64),
	//Wife, Own-child, Husband, Not-in-family, Other-relative, Unmarried.
	RELATIONSHIP_WIFE("Relationship", "Wife", 7, 65),
	RELATIONSHIP_OWN_CHILD("Relationship", "Own-child", 7, 66),
	RELATIONSHIP_HUSBAND("Relationship", "Husband", 7, 67),
	RELATIONSHIP_NOT_IN_FAMILY("Relationship", "Not-in-family", 7, 68),
	RELATIONSHIP_OTHER_RELATIVE("Relationship", "Other-relative", 7, 69),
	RELATIONSHIP_UNMARRIED("Relationship", "Unmarried", 7, 70),
	RELATIONSHIP_UNKNOWN("Relationship", "Unknown", 7, 71),
	//White, Asian-Pac-Islander, Amer-Indian-Eskimo, Other, Black
	RACE_WHITE("Race", "White", 8, 72),
	RACE_ASIAN_PAC_ISLANDER("Race", "Asian-Pac-Islander", 8, 73),
	RACE_AMER_INDIAN_ESKIMO("Race", "Amer-Indian-Eskimo", 8, 74),
	RACE_OTHER("Race", "Other", 8, 75),
	RACE_BLACK("Race", "Black", 8, 76),
	RACE_UNKNOWN("Race", "Unknown", 8, 77),
	//Female, Male.
	SEX_MALE_MALE("Sex", "Male", 9, 78),
	SEX_MALE_FEMALE("Sex", "Female", 9, 79),
	SEX_UNKNOWN("Sex", "Unknown", 9, 80),
	//0, low, high
	CAPITAL_GAIN_ZERO("Capital-gain", "Zero", 10, 81),
	CAPITAL_GAIN_LOW("Capital-gain", "Low (114-7298)", 10, 82),
	CAPITAL_GAIN_HIGH("Capital-gain", "High (7299-99999)", 10, 83),
	CAPITAL_GAIN_UNKNOWN("Capital-gain", "Unknown", 10, 84),
	//0, low, high
	CAPITAL_LOSS_ZERO("Capital-loss", " Zero", 11, 85),
	CAPITAL_LOSS_LOW("Capital-loss", " Low (155-1887)", 11, 86),
	CAPITAL_LOSS_HIGH("Capital-loss", " High (1888-4356)", 11, 87),
	CAPITAL_LOSS_UNKNOWN("Capital-loss", "Unknown", 11, 88),
	////1 - 99 range 20
	HOURS_PER_WEEK_1_20("Hours-per-week", "1-20", 12, 89),
	HOURS_PER_WEEK_21_40("Hours-per-week", "21-40", 12, 90),
	HOURS_PER_WEEK_41_60("Hours-per-week", "41-60", 12, 91),
	HOURS_PER_WEEK_61_80("Hours-per-week", "61-80", 12, 92),
	HOURS_PER_WEEK_81_100("Hours-per-week", "81-100", 12, 93),
	HOURS_PER_WEEK_UNKNOWN("Hours-per-week", "Unknown", 12, 94),
	//United-States, Cambodia, England, Puerto-Rico, Canada, Germany, Outlying-US(Guam-USVI-etc),
	//India, Japan, Greece, South, China, Cuba, Iran, Honduras, Philippines, Italy, Poland, Jamaica,
	//Vietnam, Mexico, Portugal, Ireland, France, Dominican-Republic, Laos, Ecuador, Taiwan, Haiti, 
	//Columbia, Hungary, Guatemala, Nicaragua, Scotland, Thailand, Yugoslavia, El-Salvador, 
	//Trinadad&Tobago, Peru, Hong, Holand-Netherlands
	NATIVE_COUNTRY_US("Native-country", "United-States", 13, 95), NATIVE_COUNTRY_CAMBODIA("Native-country", "Cambodia", 13, 96), NATIVE_COUNTRY_ENGLAND("Native-country", "England", 13, 97),
	NATIVE_COUNTRY_PUERTO_RICO("Native-country", "Puerto-Rico", 13, 98), NATIVE_COUNTRY_CANADA("Native-country", "Canada", 13, 99), NATIVE_COUNTRY_GERMANY("Native-country", "Germany", 13, 100),
	NATIVE_COUNTRY_OUTLYING_US("Native-country", "Outlying-US(Guam-USVI-etc)", 13, 101), NATIVE_COUNTRY_INDIA("Native-country", "India", 13, 102), NATIVE_COUNTRY_JAPAN("Native-country", "Japan", 13, 103),
	NATIVE_COUNTRY_GREECE("Native-country", "Greece", 13, 104), NATIVE_COUNTRY_SOUTH("Native-country", "South", 13, 105), NATIVE_COUNTRY_CHINA("Native-country", "China", 13, 106),
	NATIVE_COUNTRY_CUBA("Native-country", "Cuba", 13, 107), NATIVE_COUNTRY_IRAN("Native-country", "Iran", 13, 108), NATIVE_COUNTRY_HONDURAS("Native-country", "Honduras", 13, 109),
	NATIVE_COUNTRY_PHILIPPINES("Native-country", "Philippines", 13, 110), NATIVE_COUNTRY_ITALY("Native-country", "Italy", 13, 111), NATIVE_COUNTRY_POLAND("Native-country", "Poland", 13, 112), 
	NATIVE_COUNTRY_JAMAICA("Native-country", "Jamaica", 13, 113), NATIVE_COUNTRY_VIETNAM("Native-country", "Vietnam", 13, 114), NATIVE_COUNTRY_MEXICO("Native-country", "Mexico", 13, 115),
	NATIVE_COUNTRY_PORTUGAL("Native-country", "Portugal", 13, 116), NATIVE_COUNTRY_IRELAND("Native-country", "Ireland", 13, 117), NATIVE_COUNTRY_FRANCE("Native-country", "France", 13, 118),
	NATIVE_COUNTRY_DOMINICAN_REPUBLIC("Native-country", "Dominican-Republic", 13, 119), NATIVE_COUNTRY_LAOS("Native-country", "Laos", 13, 120), NATIVE_COUNTRY_ECUADOR("Native-country", "Ecuador", 13, 121),
	NATIVE_COUNTRY_TAIWAN("Native-country", "Taiwan", 13, 122), NATIVE_COUNTRY_HAITI("Native-country", "Haiti", 13, 123), NATIVE_COUNTRY_COLUMBIA("Native-country", "Columbia", 13, 124),
	NATIVE_COUNTRY_HUNGARY("Native-country", "Hungary", 13, 125), NATIVE_COUNTRY_GUATEMALA("Native-country", "Guatemala", 13, 126), NATIVE_COUNTRY_NICARAGUA("Native-country", "Nicaragua", 13, 127),
	NATIVE_COUNTRY_SCOTLAND("Native-country", "Scotland", 13, 128), NATIVE_COUNTRY_THAILAND("Native-country", "Thailand", 13, 129), NATIVE_COUNTRY_YUGOSLAVIA("Native-country", "Yugoslavia", 13, 130),
	NATIVE_COUNTRY_EL_SALVADOR("Native-country", "El-Salvador", 13, 131), NATIVE_COUNTRY_TRINIDAD("Native-country", "Trinadad&Tobago", 13, 132), NATIVE_COUNTRY_PERU("Native-country", "Peru", 13, 133),
	NATIVE_COUNTRY_HONG("Native-country", "Hong", 13, 134), NATIVE_COUNTRY_HOLAND_NETHERLANDS("Native-country", "Holand-Netherlands", 13, 135),
	NATIVE_COUNTRY_UNKNOWN("Native-country", "Unknown", 13, 136),
	SALARY_LESS_50K("Salary", "Less than 50K", 14, 137),
	SALARY_MORE_50K("Salary", "More than 50K", 14, 138),
	SALARY_UNKNOWN("Salary", "Unknown", 14, 139);
	
	private String itemTypeName;
	private String itemName;
	private int columnIndex;
	private int value;
	
	private AdultDataSetItem(String itemTypeName, String itemName, int columnIndex, int value) {
		this.itemTypeName = itemTypeName;
		this.itemName = itemName;
		this.columnIndex = columnIndex;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String getItemName() {
		return itemName;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
	
	/**
	 * Method to get the AdultDataSetItem object 
	 * given an integral value
	 * @param value
	 * @return
	 */
	public static AdultDataSetItem getItemFromValue(int value) {
		for(AdultDataSetItem adultDataSetItem : AdultDataSetItem.values()){
			if(adultDataSetItem.getValue() == value) {
				return adultDataSetItem;
			}
		}
		return null;
	}
	
	/**
	 * Method to get the AdultDataSetItem object 
	 * given an integral value and the column index
	 * @param value
	 * @return
	 */
	public static AdultDataSetItem getAttributeItem(String value, int columnIndex) {
		switch(columnIndex) {
		case 1:
			return getAgeFromAttribute(value);
		case 2: case 3: case 5: case 6: case 7: case 8: case 9: case 13:
			return getAdultDataSetItemFromAttribute(value.trim(), columnIndex);		
		case 4:
			return getEducationYearsFromAttribute(value);
		case 10:
			return getCapitalGainFromAttribute(value);
		case 11:
			return getCapitalLossFromAttribute(value);
		case 12:
			return getHoursPerWeekFromAttribute(value);
		case 14:
			return getSalaryFromAttribute(value);
		}
		return null;
	}

	
	private static AdultDataSetItem getSalaryFromAttribute(String value) {
		if(value.contains("<=50K")) {
			return AdultDataSetItem.SALARY_LESS_50K;
		}
		if(value.contains(">50K")) {
			return AdultDataSetItem.SALARY_MORE_50K;
		}
		return null;
	}

	/**
	 * Hours of week discretized into bins of 20
	 * 1-20 ; 21-40; 41-60; 61-80; 81-100
	 * @param value
	 * @return
	 */
	private static AdultDataSetItem getHoursPerWeekFromAttribute(String value) {
		int hrs = Integer.parseInt(value.trim());
		if(hrs >= 1 && hrs <= 20) {
			return HOURS_PER_WEEK_1_20;
		}
		else if(hrs >= 21 && hrs <= 40) {
			return HOURS_PER_WEEK_21_40;
		}
		else if(hrs >= 41 && hrs <= 60) {
			return HOURS_PER_WEEK_41_60;
		}
		else if(hrs >= 61 && hrs <= 80) {
			return HOURS_PER_WEEK_61_80;
		}
		else if(hrs >= 81 && hrs <= 100) {
			return HOURS_PER_WEEK_81_100;
		}
		return null;
	}

	/**
	 * Capital loss discretized into three ranges.
	 * Zero : All zero values
	 * Low : All values between zero to median of non zero values
	 * High : All values between median to max of non zero values
	 * @param value
	 * @return
	 */
	private static AdultDataSetItem getCapitalLossFromAttribute(String value) {
		int loss = Integer.parseInt(value.trim());
		if(loss == 0) {
			return CAPITAL_LOSS_ZERO;
		}
		else if(loss > 0  && loss <= 1887) {
			return CAPITAL_LOSS_LOW;
		}
		else if(loss > 1887 && loss <= 4356) {
			return CAPITAL_LOSS_HIGH;
		}
		return null;
	}

	/**
	 * Capital Gain discretized into three ranges.
	 * Zero : All zero values
	 * Low : All values between zero to median of non zero values
	 * High : All values between median to max of non zero values
	 * @param value
	 * @return
	 */
	private static AdultDataSetItem getCapitalGainFromAttribute(String value) {
		int gain = Integer.parseInt(value.trim());
		if(gain == 0) {
			return CAPITAL_GAIN_ZERO;
		}
		else if(gain > 0  && gain <= 7298) {
			return CAPITAL_GAIN_LOW;
		}
		else if(gain > 7298 && gain <= 99999) {
			return CAPITAL_GAIN_HIGH;
		}
		return null;
	}

	/**
	 * Education years discretized in bin width of 4.
	 * @param value
	 * @return
	 */
	private static AdultDataSetItem getEducationYearsFromAttribute(String value) {
		int numYears = Integer.parseInt(value.trim());
		if(numYears >= 1 && numYears <= 4) {
			return EDUCATION_NUM_1_4;
		}
		else if(numYears >= 5 && numYears <= 8) {
			return EDUCATION_NUM_5_8;
		}
		else if(numYears >= 9 && numYears <= 12) {
			return EDUCATION_NUM_9_12;
		}
		else if(numYears >= 13 && numYears <= 16) {
			return AdultDataSetItem.EDUCATION_NUM_13_16;
		}
		return null;
	}

	private static AdultDataSetItem getAdultDataSetItemFromAttribute(String value, int columnIndex) {
		if("?".equalsIgnoreCase(value)) {
			for(AdultDataSetItem adultDataSetItem : AdultDataSetItem.values()){
				if(adultDataSetItem.getColumnIndex() == columnIndex && adultDataSetItem.getItemName().equalsIgnoreCase("Unknown")) {
					return adultDataSetItem;
				}
			}
		}
		else{
			for(AdultDataSetItem adultDataSetItem : AdultDataSetItem.values()){
				if(adultDataSetItem.getItemName().equalsIgnoreCase(value)) {
					return adultDataSetItem;
				}
			}
		}
		return null;
	}

	/**
	 * Age discretized in bin widths of 10 years
	 * @param value
	 * @return
	 */
	private static AdultDataSetItem getAgeFromAttribute(String value) {
		int age = Integer.parseInt(value.trim());
		if(age >= 10 && age <= 19) {
			return AGE_10_19;
		}
		else if(age >= 20 && age <= 29) {
			return AGE_20_29;
		}
		else if(age >= 30 && age <= 39) {
			return AGE_30_39;
		}
		else if(age >= 40 && age <= 49) {
			return AGE_40_49;
		}
		else if(age >= 50 && age <= 59) {
			return AGE_50_59;
		}
		else if(age >= 60 && age <= 69) {
			return AGE_60_69;
		}
		else if(age >= 70 && age <= 79) {
			return AGE_70_79;
		}
		else if(age >= 80 && age <= 89) {
			return AGE_80_89;
		}
		else if(age >= 90 && age <= 99) {
			return AGE_90_99;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(itemTypeName);
		str.append(":");
		str.append(itemName);
		return str.toString();
	}	
}
