package com.quickjob.quickjobFinal;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by VinhNguyen on 8/11/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    //    private static final String TAG = "MyFirebaseMsgService";

    PendingIntent pendingIntent;
    String messenger, idrec, idsend, name, logocompany, avata, idcompany, namecompany;
    int typenotification;

    Bitmap image;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try{
            String mess = remoteMessage.getData().get("message");
            Log.d("MMMMM :11112",mess);
            String[] part = mess.split(" , ");
            if(part[0].equals("1")) {
                com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager session;
                session = new com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager(getApplication());
                Log.d("MMMMM :11112","2"+session.isLoggedIn());
                if (session.isLoggedIn()) {
                    if (session.isNotification()) {
                        showNotification(remoteMessage.getData().get("message"));
                        Log.d("MMMMM :11112","2222");
                    }
                }
            }else{
                com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager session;
                session = new com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager(getApplication());
                Log.d("MMMMM :11112","1"+session.isLoggedIn());
                if (session.isLoggedIn()) {
                    if (session.isNotification()) {
                        showNotification(remoteMessage.getData().get("message"));
                    }
                }
            }
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }


    }

    private void showNotification(String message) {
        Log.d("MMMMM :1111",message);
        String[] parts = message.split(" , ");
        try{
            if(parts[0].equals("0"))
            {
                com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager session;
                session = new com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager(getApplication());
                if(parts.length>3) {
                    String avata = parts[1];
                    String name = parts[2];
                    String mess = parts[3];
                    String idrec = parts[4];

                    if (avata.equals("") || avata == null) {
                        image = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.logo_hire);
                    } else {
                        try {
                            URL url = new URL(avata);
                            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch(IOException e) {
                            System.out.println(e);
                        }
                    }
                    Log.d(TAG,avata);
                    Log.d(TAG,name);
                    Log.d(TAG,mess);
                    messenger=mess;
                    HashMap<String, String> user = session.getUserDetails();

                    Intent intent = new Intent(this,com.quickjob.quickjobFinal.quickjobHire.activity.ChattingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("chat", false);
                    intent.putExtra("idsend",user.get(session.KEY_ID).substring(0,14));
                    intent.putExtra("idrec",idrec);
                    intent.putExtra("name",user.get(session.KEY_NAME));
                    intent.putExtra("logocompany",avata);
                    intent.putExtra("avata",user.get(session.KEY_LOGO));
                    intent.putExtra("idcompany",idrec);
                    intent.putExtra("namecompany",name);
                    Log.d("idsend",user.get(session.KEY_ID).substring(0,14));
                    Log.d("idrec",idrec);
                    Log.d("name",user.get(session.KEY_NAME));
                    Log.d("logocompany",avata);
                    Log.d("avata",user.get(session.KEY_LOGO));
                    Log.d("idcompany",idrec);
                    Log.d("namecompany",name);
                    pendingIntent = PendingIntent.getActivity(this, 100, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                    typenotification = 1;
                    Uri defaultSoundUri =
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notif = new Notification.Builder(this)
                            .setContentIntent(pendingIntent)
                            .setContentTitle(name)
                            .setContentText(messenger)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setLights(Color.RED, 3000, 3000)
                            .setSound(defaultSoundUri)
                            .setSmallIcon(R.mipmap.ic_app)
                            .setLargeIcon(getCroppedBitmap(image))
                            .build();
                    notif.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(typenotification, notif);
                }else {
                    Intent intent = new Intent(this,com.quickjob.quickjobFinal.quickjobHire.activity.MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("noti", false);
                    pendingIntent = PendingIntent.getActivity(this, 100, intent,
                            PendingIntent.FLAG_ONE_SHOT);

                    typenotification = 2;
                    Uri defaultSoundUri =
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notif = null;
                    notif = new Notification.Builder(this)
                            .setContentIntent(pendingIntent)
                            .setContentTitle(getResources().getString(R.string.titlenotification))
                            .setContentText(message.split(" , ")[1] + " " + getResources().getString(R.string.textnotification) + " " + message.split(" , ")[2])
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setLights(Color.RED, 3000, 3000)
                            .setSound(defaultSoundUri)
                            .setSmallIcon(R.mipmap.ic_app)
                            .build();

                    notif.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(typenotification, notif);
                    Intent a = new Intent();
                    a.setAction("appendChatScreenMsg");
                    a.putExtra("reload", true);
                    this.sendBroadcast(a);

                    Intent x = new Intent();
                    x.setAction("productdetail");
                    x.putExtra("reload", true);
                    this.sendBroadcast(x);
                }
            }else{
                com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager session;
                session = new com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager(getApplication());
                if (parts.length > 3) {
                    String avata = parts[1];
                    String name = parts[2];
                    String mess = parts[3];
                    String idrec = parts[4];

                    if (avata.equals("") || avata == null) {
                        image = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.logo2);
                    } else {
                        try {
                            URL url = new URL(avata);
                            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    }

                    Log.d(TAG, avata);
                    Log.d(TAG, name);
                    Log.d(TAG, mess);
                    messenger = mess;
                    Intent intent = new Intent(this,com.quickjob.quickjobFinal.quickjobFind.activity.ChattingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("chat", false);
                    intent.putExtra("idsend",session.getId().substring(0,14));
                    intent.putExtra("idrec",idrec);
                    intent.putExtra("name",session.getName());
                    intent.putExtra("logocompany",avata);
                    intent.putExtra("avata",session.getLogo());
                    intent.putExtra("idcompany",idrec);
                    intent.putExtra("namecompany",name);
                    Log.d("idsend",session.getId().substring(0,14));
                    Log.d("idrec",idrec);
                    Log.d("name",session.getName());
                    Log.d("logocompany",avata);
                    Log.d("avata",session.getLogo());
                    Log.d("idcompany",idrec);
                    Log.d("namecompany",name);
                    pendingIntent = PendingIntent.getActivity(this, 100, intent,
                            PendingIntent.FLAG_ONE_SHOT);
//        messenger = "bbbbbbbbbbb";
                    typenotification = 0;
                    Uri defaultSoundUri =
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notif = new Notification.Builder(this)
                            .setContentIntent(pendingIntent)
                            .setContentTitle(name)
                            .setContentText(messenger)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setLights(Color.RED, 3000, 3000)
                            .setSound(defaultSoundUri)
                            .setSmallIcon(R.mipmap.ic_app)
                            .setLargeIcon(getCroppedBitmap(image))
                            .build();
                    notif.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(typenotification, notif);
                    Intent a = new Intent();
                    a.setAction("appendChatScreenMsg");
                    a.putExtra("reload", true);
                    this.sendBroadcast(a);

                    Intent x = new Intent();
                    x.setAction("productdetail");
                    x.putExtra("reload", true);
                    this.sendBroadcast(x);
                } else {
                    if (parts[2].equals("0") || parts[2].equals("1")) {
                        Intent intent = new Intent(this,com.quickjob.quickjobFinal.quickjobFind.activity.MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("apply", false);
                        pendingIntent = PendingIntent.getActivity(this, 100, intent,
                                PendingIntent.FLAG_ONE_SHOT);
                        if (parts[2].equals("0"))
                            messenger = parts[1] + getResources().getString(R.string.acceptcv);
                        else if (parts[2].equals("1"))
                            messenger = parts[1] + " " + getResources().getString(R.string.refusecv);
                        Log.e("Loi", messenger);
                        typenotification = 1;
                    } else if (parts[2].equals("2")) {
                        Intent intent = new Intent(this,com.quickjob.quickjobFinal.quickjobFind.activity.MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("save", false);
                        pendingIntent = PendingIntent.getActivity(this, 100, intent,
                                PendingIntent.FLAG_ONE_SHOT);
                        messenger = "(" + parts[1] + ") " + getResources().getString(R.string.foundjob);
                        typenotification = 2;

                    }else if(parts[2].equals("3")){
                        String[] notidel = parts[1].split(" / ");
                        messenger = getResources().getString(R.string.job)+" "+notidel[1]+" "+getResources().getString(R.string.removed)+" "+notidel[0];
                        typenotification = 3;
                    }else if(parts[2].equals("4")){
                        String[] notidel = parts[1].split(" / ");
                        messenger = getResources().getString(R.string.job)+" "+notidel[1]+" "+getResources().getString(R.string.changed)+" "+notidel[0];
                        typenotification = 3;
                    }
                    Uri defaultSoundUri =
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notif = new Notification.Builder(this)
                            .setContentIntent(pendingIntent)
                            .setContentTitle(getResources().getString(R.string.QJNotification))
                            .setContentText(messenger)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setLights(Color.RED, 3000, 3000)
                            .setSound(defaultSoundUri)
                            .setSmallIcon(R.mipmap.ic_app)
                            .build();
                    notif.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(typenotification, notif);
                    Intent a = new Intent();
                    a.setAction("appendChatScreenMsg");
                    a.putExtra("reload", true);
                    this.sendBroadcast(a);

                    Intent x = new Intent();
                    x.setAction("productdetail");
                    x.putExtra("reload", true);
                    this.sendBroadcast(x);
                }
            }
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notif = new Notification.Builder(mContext)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_app)
                    .setLargeIcon(result)
                    .setStyle(new Notification.BigPictureStyle().bigPicture(result))
                    .build();
            notif.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(1, notif);
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
//        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
