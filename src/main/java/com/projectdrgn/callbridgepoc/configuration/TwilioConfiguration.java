package com.projectdrgn.callbridgepoc.configuration;


public class TwilioConfiguration {

    public static final String ACCOUNT_SID = "--"; // your Account SID found in the Twilio Console
    public static final String AUTH_TOKEN = "--"; // your auth token also found in the Twilio Console

    // use the +12025551234 format for the value in the following constant
    public static final String TWILIO_NUMBER = "+12058102264"; // Twilio phone number for dialing outbound phone calls

    public static final String NGROK_BASE_URL = "https://b3366cb2.ngrok.io"; // paste your ngrok Forwarding URL such as https://0e64e563.ngrok.io

    public static String phoneNumber = "";

    public static String demoXmlUrl = "http://demo.twilio.com/docs/voice.xml";
}
