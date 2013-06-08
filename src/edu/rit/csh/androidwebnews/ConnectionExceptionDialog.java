/**
 See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  This code is licensed
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package edu.rit.csh.androidwebnews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

class ConnectionExceptionDialog extends AlertDialog {

    public ConnectionExceptionDialog(Context context, String message) {
        super(context);
        setTitle("Connection Exception");
        setMessage(message);
        setCancelable(true);
        setButton(BUTTON_NEGATIVE, "OK", new cancelListener());

    }

    ConnectionExceptionDialog(Context context) {
        super(context);
        setTitle("Connection Exception");
        setCancelable(true);
        setButton(BUTTON_NEGATIVE, "OK", new cancelListener());

    }


    private final class cancelListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }

    }
}
