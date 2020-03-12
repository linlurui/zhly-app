package com.yizhitong.wisdombuilding;

public class Constants {

    public static final String DateFormat_10 = "yyyy-MM-dd";

    public static final String ISUPDATE = "isUpdate";

    public static final String MDOWNLOADURL = "mDownloadUrl";

    public static final String ISSHOWUWHITE = "isShowUWhite";

    public static final String KeySavedUsername = "KeySavedUsername";

    public static final String ExtraScanType = "ExtraScanType";

    public static final String ExtraId = "ExtraId";

    public static final String ExtraFrontCamera = "ExtraFrontCamera";

    public static final String EMPLOYEEID ="employeeId";

    public static final String  TITLE="title";

    public static final String  TAG="tag";

    public static final String  TIME="time";

    public enum ScanType {
        IdCardFront, IdCardBack, BankCard, AddBankCard;

        public String getTypeString() {
            switch (this) {
                case IdCardFront:
                    return "face";
                case IdCardBack:
                    return "back";
                case BankCard:
                    return "card";
                case AddBankCard:
                    return "addCard";
            }
            return "";
        }
    }
}
