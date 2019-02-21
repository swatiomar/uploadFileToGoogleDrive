package com.example.mobua01.uploadfiletocloud;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

/**
 * Created by mobua01 on 23/10/18.
 */

public class UserAccountTask extends AsyncTask<Void, Void, FullAccount> {

    private DbxClientV2 dbxClient;
    private AccountDetailListener accountDetailListener;
    private Exception error;

    public interface AccountDetailListener {
        void onAccountReceived(FullAccount account);
        void onError(Exception error);
    }

    UserAccountTask(DbxClientV2 dbxClient, AccountDetailListener accountDetailListener){
        this.dbxClient =dbxClient;
        this.accountDetailListener = accountDetailListener;
    }

    @Override
    protected FullAccount doInBackground(Void... params) {
        try {
            //get the users FullAccount
            return dbxClient.users().getCurrentAccount();
        } catch (DbxException e) {
            e.printStackTrace();
            error = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(FullAccount account) {
        super.onPostExecute(account);

        if (account != null && error == null){
            //User Account received successfully
            accountDetailListener.onAccountReceived(account);
        }
        else {
            // Something went wrong
            accountDetailListener.onError(error);
        }
    }
}
