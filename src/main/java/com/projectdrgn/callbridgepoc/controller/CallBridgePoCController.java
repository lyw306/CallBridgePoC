package com.projectdrgn.callbridgepoc.controller;

import com.projectdrgn.callbridgepoc.model.DialStatus;
import com.projectdrgn.callbridgepoc.model.RecordStatus;
import com.projectdrgn.callbridgepoc.service.CallBridgeService;
import com.projectdrgn.callbridgepoc.utils.FileUtil;
import com.twilio.twiml.TwiMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@RestController
public class CallBridgePoCController {

    @Autowired
    public CallBridgeService callBridgeService;

    @RequestMapping(value = "/dialPhone", method = RequestMethod.GET)
    private String startOneWayCall(@RequestParam String toNumber) {
        System.out.println("RequestParam:" + toNumber);
        callBridgeService.startPhoneCall(toNumber);
        return "success";
    }

    @RequestMapping(value = "/twiml", method = RequestMethod.POST)
    private String twiml(@RequestBody String call) {
        callBridgeService.twiml();
        return "success";
    }

    @RequestMapping(value = "/callBridge", method = RequestMethod.GET)
    private String startCallBridge(@RequestParam String custNum, @RequestParam String agntNum) {
        System.out.println("RequestParam:" + custNum + "," + agntNum);
        callBridgeService.startCallBridge(custNum, agntNum);
        return "success";
    }

    @RequestMapping(value = "/twimlBridge/{agntNum}", method = RequestMethod.POST, produces = "application/xml")
    public ResponseEntity<String> twimlBridge(@PathVariable String agntNum, @RequestBody String call, HttpServletRequest request) throws TwiMLException {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);

        return new ResponseEntity<>(
                callBridgeService.twimlBridge(agntNum),
                httpHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/recordCallBack", method = RequestMethod.POST)
    private String recordCallBack(@RequestBody String record) {
        record = FileUtil.StringEncodeTransfer(record);
        System.out.println("record:" + record);
        callBridgeService.saveRecordStatusAndDownloadAudio(record);
        return "success";
    }

    @RequestMapping(value = "/dialCallBack", method = RequestMethod.POST)
    private String dialCallBack(@RequestBody String dialStatus) {
        dialStatus = FileUtil.StringEncodeTransfer(dialStatus);

        System.out.println("dialStatus:" + dialStatus);

        callBridgeService.saveDialStatus(dialStatus);
        return "success";
    }

    @RequestMapping(value = "/getDialStatus", method = RequestMethod.GET)
    private List<DialStatus> getDialStatus() {

        return callBridgeService.getDialStatus();
    }

    @RequestMapping(value = "/getRecordStatus", method = RequestMethod.GET)
    private List<RecordStatus> getRecordStatus() {

        return callBridgeService.getRecordStatus();
    }

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    private String health() {
        return "app up and running";
    }
}

