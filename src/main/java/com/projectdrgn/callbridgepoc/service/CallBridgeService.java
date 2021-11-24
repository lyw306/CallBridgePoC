package com.projectdrgn.callbridgepoc.service;

import com.alibaba.fastjson.JSONObject;
import com.projectdrgn.callbridgepoc.Dao.DialStatusRepository;
import com.projectdrgn.callbridgepoc.Dao.RecordStatusRepository;
import com.projectdrgn.callbridgepoc.configuration.TwilioConfiguration;
import com.projectdrgn.callbridgepoc.model.DialStatus;
import com.projectdrgn.callbridgepoc.model.RecordStatus;
import com.projectdrgn.callbridgepoc.utils.BeanToMapUtil;
import com.projectdrgn.callbridgepoc.utils.FileUtil;
import com.projectdrgn.callbridgepoc.utils.RestUtil;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Number;
import com.twilio.twiml.voice.*;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CallBridgeService {

    // instantiate the TwilioRestClient helper library with our Twilio credentials set as constants
    public static TwilioRestClient client = new TwilioRestClient.Builder(TwilioConfiguration.ACCOUNT_SID, TwilioConfiguration.AUTH_TOKEN).build();
    @Autowired
    private DialStatusRepository dialStatusRepository;
    @Autowired
    private RecordStatusRepository recordStatusRepository;

    public String startPhoneCall(String toNumber) {
        PhoneNumber to = new PhoneNumber(toNumber);
        PhoneNumber from = new PhoneNumber(TwilioConfiguration.TWILIO_NUMBER);
        URI uri = URI.create(TwilioConfiguration.NGROK_BASE_URL + "/twiml");

        // Make the call using the TwilioRestClient we instantiated
        Call call = new CallCreator(to, from, uri).create(client);
        return "success";
    }

    public String twiml() {
        URI recordCallBack = URI.create(TwilioConfiguration.NGROK_BASE_URL + "/recordCallBack");
        Record record = new Record.Builder().timeout(10).transcribe(false).action(recordCallBack)
                .build();
        Say sayHello = new Say.Builder("Hello, I am a Tech Support").build();
        Play playSong = new Play.Builder("https://api.twilio.com/cowbell.mp3").build();
        VoiceResponse voiceResponse = new VoiceResponse.Builder().say(sayHello).play(playSong).record(record).build();
        return voiceResponse.toXml();
    }

    public String startCallBridge(String custNum, String agntNum) {
        PhoneNumber to = new PhoneNumber(custNum);
        PhoneNumber from = new PhoneNumber(TwilioConfiguration.TWILIO_NUMBER);

        try {
            URI uri = URI.create(TwilioConfiguration.NGROK_BASE_URL + "/twimlBridge/" + URLEncoder.encode(agntNum, "UTF-8"));
            Call call = new CallCreator(to, from, uri).create(client);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "failed";
        }
        return "success";
    }

    public String twimlBridge(String agntNum) {
        URI recordCallBack = URI.create(TwilioConfiguration.NGROK_BASE_URL + "/recordCallBack");
        URI dialCallBack = URI.create(TwilioConfiguration.NGROK_BASE_URL + "/dialCallBack");
        Number number = new Number.Builder(agntNum).build();
//        Record record = new Record.Builder().timeout(10).transcribe(false).action(recordCallBack)
//                .build();
        return new VoiceResponse.Builder()
                .say(new Say.Builder("Hello, I am a Tech Support, I will connect you to the agent's call now.").build())
                .dial(new Dial.Builder().number(number).record(Dial.Record.RECORD_FROM_RINGING_DUAL)
                        .action(dialCallBack).recordingStatusCallback(recordCallBack).build())
                .build()
                .toXml();
    }

    public String saveDialStatus(String status) {

        try {
            Map<String, Object> map = FileUtil.parseMap(status);

            DialStatus dialStatus = BeanToMapUtil.map2bean(map, DialStatus.class);
            dialStatusRepository.save(dialStatus);

            JSONObject json = new JSONObject(map);
            FileUtil.writeJsonfile(json.toJSONString(), FileUtil.dialStatusFile);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    public String saveRecordStatusAndDownloadAudio(String record) {

        try {
            Map<String, Object> map = FileUtil.parseMap(record);
//            Map<String,Object> map = (Map)JSONObject.parse(record);

            RecordStatus recordStatus = BeanToMapUtil.map2bean(map, RecordStatus.class);
            recordStatusRepository.save(recordStatus);

            JSONObject json = new JSONObject(map);
            FileUtil.writeJsonfile(json.toJSONString(), FileUtil.recordFile);

            String RecordingStatus = json.getString("RecordingStatus");
            String RecordingUrl = json.getString("RecordingUrl");
            String fileName = json.getString("AccountSid") + "-" + RestUtil.sdf.format(new Date());
            if ("completed".equals(RecordingStatus)) {
                RestUtil.download(fileName, RecordingUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    public List<DialStatus> getDialStatus() {
        return dialStatusRepository.findAll();
    }

    public List<RecordStatus> getRecordStatus() {
        return recordStatusRepository.findAll();
    }
}