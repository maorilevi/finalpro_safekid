package com.parse.starter;

import java.util.ArrayList;

/**
 * Created by Admin on 20/02/2016.
 */
public class EventArray {
    private ArrayList<Event> listevent;

    public EventArray() {
    }
    public EventArray(ArrayList<Event> listevent) {
        this.listevent = listevent;
    }

    public ArrayList<Event> getListevent() {
        return listevent;
    }
    public void setListevent(ArrayList<Event> listevent) {
        this.listevent = listevent;
    }

    public void DELETEALLEVENTS(){
        this.listevent.clear();
    }


    public ArrayList<Event> GET_EVENTS_BY_KID_ID_AND_DAY(String kidid,String day){
        ArrayList<Event> kidevents=new ArrayList<Event>();
        for(int indx=0;indx<listevent.size();indx++){
            if(listevent.get(indx).getKidID().matches(kidid)&&listevent.get(indx).getDay().matches(day)){
                kidevents.add(listevent.get(indx));
            }
        }
        return kidevents;
    }
    public ArrayList<Event> GET_EVENTS_BY_KID_ID(String kidid){
        ArrayList<Event> kidevents=new ArrayList<Event>();
        for(int indx=0;indx<listevent.size();indx++){
            if(listevent.get(indx).getKidID().matches(kidid)){
                kidevents.add(listevent.get(indx));
            }
        }
        return kidevents;
    }
    public int LIST_SIZE(){
        return listevent.size();
    }
}
