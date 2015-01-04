/*
 * Copyright (C) 2015 Mehmet Akif Tütüncü
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mehmetakiftutuncu.mykentkart.utilities;

/**
 * A utility class containing constant definitions used throughout the application
 *
 * @author mehmetakiftutuncu
 */
public class Constants {
    /** Package name of the application */
    public static final String PACKAGE_NAME = "com.mehmetakiftutuncu.mykentkart";

    /** Tag for name of a KentKart */
    public static final String KENT_KART_NAME         = PACKAGE_NAME + ".kentkart.name";
    /** Tag for number of a KentKart */
    public static final String KENT_KART_NUMBER       = PACKAGE_NAME + ".kentkart.number";
    /** Tag for NFC id of a KentKart */
    public static final String KENT_KART_NFC_ID       = PACKAGE_NAME + ".kentkart.nfcid";
    /** Tag for region code of a KentKart */
    public static final String KENT_KART_REGION_CODE  = PACKAGE_NAME + ".kentkart.regioncode";

    /** Tag for running in edit mode */
    public static final String EDIT_MODE              = PACKAGE_NAME + ".editmode";
    /** Tag for running with an NFC id */
    public static final String HAS_NFC                = PACKAGE_NAME + ".hasnfc";
    /** Tag for the current state of the process */
    public static final String STATE                  = PACKAGE_NAME + ".state";
    /** Tag for the state of NFC dialog */
    public static final String IS_NFC_DIALOG_ANSWERED = PACKAGE_NAME + ".isnfcdialoganswered";
    /** Tag for the state of current KentKart list */
    public static final String KENT_KART_LIST         = PACKAGE_NAME + ".kentkartlist";
    /** Tag for the state of current KentKart information */
    public static final String KENT_KART_INFORMATION  = PACKAGE_NAME + ".kentkartinformation";

    /** Tag for the title of help page */
    public static final String HELP_TITLE             = PACKAGE_NAME + ".title";
    /** Tag for the image of help page */
    public static final String HELP_IMAGE             = PACKAGE_NAME + ".image";
    /** Tag for the message of help page */
    public static final String HELP_MESSAGE           = PACKAGE_NAME + ".message";
    /** Tag for the flag indicating if help is started manually */
    public static final String HELP_STARTED_MANUALLY  = PACKAGE_NAME + ".startedmanually";

    /** Preferred format to show date and time, example: 03 January 2015, 22:37 */
    public static final String DATE_TIME_FORMAT                    = "dd MMMM yyyy, HH:mm";
    /** Format of date and time that KentKart servers use, example: 03.01.2015 22:37:05 */
    public static final String KENT_KART_RESPONSE_DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";

    /** Relative path to folder in which KentKart data will be stored */
    public static final String DATA_PATH = "/Android/data/" + PACKAGE_NAME;

    /** Key of connected transport enabled preference */
    public static final String PREFERENCE_CONNECTED_TRANSPORT_ENABLED  = "preference_connectedTransport_enabled";
    /** Key of connected transport duration preference */
    public static final String PREFERENCE_CONNECTED_TRANSPORT_DURATION = "preference_connectedTransport_duration";

    /** Key of rate item in about section */
    public static final String PREFERENCE_ABOUT_RATE     = "preference_about_rate";
    /** Key of help item in about section */
    public static final String PREFERENCE_ABOUT_HELP     = "preference_about_help";
    /** Key of feedback item in about section */
    public static final String PREFERENCE_ABOUT_FEEDBACK = "preference_about_feedback";
    /** Key of version item in about section */
    public static final String PREFERENCE_ABOUT_VERSION  = "preference_about_version";
    /** Key of licenses item in about section */
    public static final String PREFERENCE_ABOUT_LICENSES = "preference_about_licenses";

    /** Key of completion state of help dialog */
    public static final String PREFERENCE_IS_HELP_COMPLETED = "preference_is_help_completed";

    /** Number of launch times after which rate application bar will be shown */
    public static final int RATE_LAUNCH_COUNT = 3;

    /** URI of the application page on Google Play */
    public static final String RATE_URI = "market://details?id=" + PACKAGE_NAME;

    /** E-mail address of the contact to receive feedback e-mails */
    public static final String FEEDBACK_CONTACT = "m.akif.tutuncu@gmail.com";
}
