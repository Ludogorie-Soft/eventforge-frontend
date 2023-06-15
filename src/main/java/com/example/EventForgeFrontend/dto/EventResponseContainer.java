package com.example.EventForgeFrontend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
public class EventResponseContainer {
    private List<OneTimeEventResponse> oneTimeEvents = new ArrayList<>();
    private List<RecurrenceEventResponse> recurrenceEvents =new ArrayList<>();

    public EventResponseContainer(List<OneTimeEventResponse> oneTimeEventResponses , List<RecurrenceEventResponse> recurrenceEventResponses){
        addOneTimeEventsToList(oneTimeEventResponses);
        addRecurrenceEventsToList(recurrenceEventResponses);
    }

    public void addOneTimeEventsToList(List<OneTimeEventResponse> oneTimeEvents){
        if(oneTimeEvents!=null){
            this.oneTimeEvents.addAll(oneTimeEvents);
        }
    }

    public void addRecurrenceEventsToList(List<RecurrenceEventResponse> recurrenceEvents){
        if(recurrenceEvents!=null){
            this.recurrenceEvents.addAll(recurrenceEvents);
        }
    }
}
