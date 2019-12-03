package com.dal.mc.servicegenie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.Calendar;

public class BookingService extends AppCompatActivity {

    EditText datetimeedt;
    //EditText tmp;
    Calendar serviceDateTimeCalendar;
    //RangeTimePickerDialog tpd;
    TimePickerDialog tpd;
    //Button location;
    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_service);

        gps = new GPSTracker(BookingService.this);
        gps.getLocation();


        datetimeedt = (EditText) findViewById(R.id.datetimeedt);
        //location = (Button) findViewById(R.id.location);
        //tmp = (EditText) findViewById(R.id.tmp);

//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gps.getLocation();
//                if (gps.canGetLocation()) {
//                    tmp.setText(gps.getLatitude());
//                } else {
//                    gps.showSettingsAlert();
//                }
//            }
//        });


        datetimeedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //custom dialog
                final Dialog dialog = new Dialog(BookingService.this);
                dialog.setContentView(R.layout.datetimedialog);

                //set custom dialog component
                final DatePickerTimeline1 dateTimeLinePicker = (DatePickerTimeline1) dialog.findViewById(R.id.dateTimeLinePicker);
                final TextView timeSelected = (TextView) dialog.findViewById(R.id.timeSelected);

                Button selectDateTimeBtn = (Button) dialog.findViewById(R.id.selectDateTime);

                //set a start date
                Calendar currentDate = Calendar.getInstance();
                dateTimeLinePicker.setInitialDate(currentDate.get(Calendar.YEAR), (currentDate.get(Calendar.MONTH)), currentDate.get(Calendar.DAY_OF_MONTH));

                //Today's date
                //Toast.makeText(BookingService.this, "Y: "+calendar.get(Calendar.YEAR)+"\nM: "+(calendar.get(Calendar.MONTH)+1)+"\nD: "+calendar.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();

                //set date selected listener
                dateTimeLinePicker.setOnDateSelectedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int month, int day, int dayOfWeek) {

                        //Selected Date
                        Toast.makeText(BookingService.this, "Y+: "+year+"\nM: "+month+"\nD: "+day, Toast.LENGTH_SHORT).show();


                        serviceDateTimeCalendar = Calendar.getInstance();
                        serviceDateTimeCalendar.set(year, month, day);

                        //Toast.makeText(BookingService.this, , Toast.LENGTH_SHORT).show();
                        //return calendar.getTime();
                        if ((year == serviceDateTimeCalendar.get(Calendar.YEAR)) && (month + 1 == serviceDateTimeCalendar.get(Calendar.MONTH)) && (day == serviceDateTimeCalendar.get(Calendar.DAY_OF_MONTH))) {
                            //client can book service after an hour of current time
                            int afterMintuesBooking = 60;

                            //Current hour and minute
                            //Toast.makeText(BookingService.this,"H: "+(calendar.HOUR)+"\nM: "+(calendar.MINUTE),Toast.LENGTH_SHORT).show();
                            final int hour = (serviceDateTimeCalendar.get(Calendar.HOUR) + (afterMintuesBooking / 60));
                            int min = (serviceDateTimeCalendar.get(Calendar.MINUTE) + (afterMintuesBooking % 60));

                            //processed hour and minute
                            //Toast.makeText(BookingService.this,"H: "+hour+"\nM: "+min,Toast.LENGTH_SHORT).show();

//                            tpd = new RangeTimePickerDialog(BookingService.this, new RangeTimePickerDialog.OnTimeSetListener() {
//                                @Override
//                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                                }
//                            }, hour, min, true);
//                            tpd.setMin(hour, min);
//                            tpd.setMax(24, 60);
//                            tpd.show();
                            tpd = new TimePickerDialog(BookingService.this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



                                    serviceDateTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    serviceDateTimeCalendar.set(Calendar.MINUTE, minute);
                                    serviceDateTimeCalendar.set(Calendar.SECOND, 0);
                                }
                            },hour,min,true);
                            tpd.show();


                        } else {

                            Toast.makeText(BookingService.this,"Hello",Toast.LENGTH_SHORT).show();
                            tpd = new TimePickerDialog(BookingService.this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    serviceDateTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    serviceDateTimeCalendar.set(Calendar.MINUTE, minute);
                                    serviceDateTimeCalendar.set(Calendar.SECOND, 0);

                                }
                            },0,0,true);
                            tpd.show();
                        }

                        tpd.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    //Toast.makeText(getApplicationContext(), serviceDateTimeCalendar.getTime().toString(), Toast.LENGTH_LONG).show();

//                                    serviceDateTimeCalendar.set(Calendar.HOUR_OF_DAY, tpd.currentHour);
//                                    serviceDateTimeCalendar.set(Calendar.MINUTE, tpd.currentMinute);
//                                    serviceDateTimeCalendar.set(Calendar.SECOND, 00);

                                    timeSelected.setText(serviceDateTimeCalendar.getTime().toString());
                                }
                            }
                        });
                    }

                    @Override
                    public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {

                    }
                });

                dialog.show();

                selectDateTimeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //tpd.dismiss();
                        dialog.dismiss();


                        Toast.makeText(getApplicationContext(), "Time: " + serviceDateTimeCalendar.getTime(), Toast.LENGTH_LONG).show();
                        datetimeedt.setText(serviceDateTimeCalendar.getTime().toString());

                    }
                });

            }
        });


    }


}
