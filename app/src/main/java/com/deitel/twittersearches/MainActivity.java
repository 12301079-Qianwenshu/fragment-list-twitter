// MainActivity.java
// Manages your favorite Twitter searches for easy  
// access and display in the device's web browser
package com.deitel.twittersearches;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;


public class MainActivity extends Activity implements Fragment1.OnFragmentInteractionListener {
    // name of SharedPreferences XML file that stores the saved searches
    private static final String SEARCHES = "searches";

    private EditText queryEditText; // EditText where user enters a query
    private EditText tagEditText; // EditText where user tags a query
    private SharedPreferences savedSearches; // user's favorite searches
    private FragmentManager fm;
    private Fragment1 fragment1;
    private Fragment2 fragment2;

    // SOME Changes _ called when MainActivity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get references to the EditTexts
        queryEditText = (EditText) findViewById(R.id.queryEditText);
        tagEditText = (EditText) findViewById(R.id.tagEditText);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();

        // get the SharedPreferences containing the user's saved searches
        savedSearches = getSharedPreferences(SEARCHES, MODE_PRIVATE);

        fragment1.setSavedSearch(savedSearches);

        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, fragment1);
        ft.commit();

        // register listener to save a new or edited search
        ImageButton saveButton =
                (ImageButton) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);

        // MOVE to ListFragment _ register listener that searches Twitter when user touches a tag
        //getListView().setOnItemClickListener(itemClickListener);

        // MOVE to ListFragment _  set listener that allows user to delete or edit a search
        //getListView().setOnItemLongClickListener(itemLongClickListener);
    } // end method onCreate

    // NO CHANGES _  saveButtonListener saves a tag-query pair into SharedPreferences
    public OnClickListener saveButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // create tag if neither queryEditText nor tagEditText is empty
            if (queryEditText.getText().length() > 0 &&
                    tagEditText.getText().length() > 0) {
                addTaggedSearch(queryEditText.getText().toString(),
                        tagEditText.getText().toString());
                queryEditText.setText(""); // clear queryEditText
                tagEditText.setText(""); // clear tagEditText

                ((InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        tagEditText.getWindowToken(), 0);
            } else // display message asking user to provide a query and a tag
            {
                // create a new AlertDialog Builder
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this);

                // set dialog's message to display
                builder.setMessage(R.string.missingMessage);

                // provide an OK button that simply dismisses the dialog
                builder.setPositiveButton(R.string.OK, null);

                // create AlertDialog from the AlertDialog.Builder
                AlertDialog errorDialog = builder.create();
                errorDialog.show(); // display the modal dialog
            }
        } // end method onClick
    }; // end OnClickListener anonymous inner class

    // NO CHANGES _  add new search to the save file, then refresh all Buttons
    private void addTaggedSearch(String query, String tag) {
        fragment1.addSavedSearch(query, tag);
        fragment1.addTags(tag);
    }

    @Override
    public void onShowWebsites(String urlString) {
        fragment2.setUrlString(urlString);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, fragment2)
                .addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onUpdateView(String query, String tag) {
        tagEditText.setText(tag);
        queryEditText.setText(query);
    }
} // end class MainActivity

/**************************************************************************
 * (C) Copyright 1992-2014 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 **************************************************************************/