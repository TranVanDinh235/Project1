package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.example.project1.MainActivity.handleSamplingAndRotationBitmap;

public class ListActivity extends AppCompatActivity {
    private static Identification identification;

    private IdentificationViewModel identificationViewModel;
    private TextView processingTextView;
    private TextView processedTextView;
    private View processingView;
    private View proceesedView;
    private EditText GTCNTypeEditText;
    private EditText GTCNNumberEditText;
    private EditText fullNameEditText;
    private EditText dayOfBirthEditText;
    private EditText genderEditText;
    private EditText nativePlaceEditText;
    private EditText datedEditText;
    private EditText placeEditText;
    private ImageView frontImageView;
    private ImageView backSideImageView;
    private Button editButton;
    private Button doneButton;
    private ImageButton newButton;

    private LinearLayout listLayout;
    private LinearLayout detailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        final IdentificationListAdapter adapter = new IdentificationListAdapter(this, new ItemClickListener() {
            @Override
            public void onItemClickListener(Identification id) {
                identification = id;
                listLayout.setVisibility(View.GONE);
                detailLayout.setVisibility(View.VISIBLE);
                setTextEditText(id);
            }

            @Override
            public void onDeleteItem(Identification id) {
                identification = id;
                new DeleteDialog(identificationViewModel).show(getSupportFragmentManager(), "dialog");
            }

            @Override
            public void onEditItem(Identification id) {
                identification = id;
                listLayout.setVisibility(View.GONE);
                detailLayout.setVisibility(View.VISIBLE);
                setTextEditText(id);
                enableEditText();
                doneButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        identificationViewModel = ViewModelProviders.of(this).get(IdentificationViewModel.class);
        identificationViewModel.getAllIdentityCards().observe(this, new Observer<List<Identification>>() {
            @Override
            public void onChanged(@Nullable final List<Identification> identifications) {
                adapter.setIdentifications(identifications);
            }
        });
        initView();
        disableEditText();
    }

    void initView() {
        listLayout = findViewById(R.id.list_layout);
        detailLayout = findViewById(R.id.detail_layout);
        proceesedView = findViewById(R.id.v_processed);
        processingView = findViewById(R.id.v_processing);
        processedTextView = findViewById(R.id.tv_processed);
        processingTextView = findViewById(R.id.tv_processing);
        processedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceesedView.setVisibility(View.VISIBLE);
                processingView.setVisibility(View.INVISIBLE);
            }
        });
        processingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceesedView.setVisibility(View.INVISIBLE);
                processingView.setVisibility(View.VISIBLE);
            }
        });

        doneButton = findViewById(R.id.btn_done);
        editButton = findViewById(R.id.btn_edit_profile);
        GTCNTypeEditText = findViewById(R.id.edt_GTCN_type);
        GTCNNumberEditText = findViewById(R.id.edt_GTCN_number);
        fullNameEditText = findViewById(R.id.edt_name);
        dayOfBirthEditText = findViewById(R.id.edt_birthday);
        genderEditText = findViewById(R.id.edt_gender);
        nativePlaceEditText = findViewById(R.id.edt_native_place);
        datedEditText = findViewById(R.id.edt_dated);
        placeEditText = findViewById(R.id.edt_place);
        frontImageView = findViewById(R.id.imv_front_image);
        backSideImageView = findViewById(R.id.imv_backside_image);
        newButton = findViewById(R.id.btn_new);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditText();
                editButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditText();
                getTextEditText();
                identificationViewModel.update(identification);
                doneButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                detailLayout.setVisibility(View.GONE);
                listLayout.setVisibility(View.VISIBLE);
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        });

    }

    void setTextEditText(Identification identification){
        GTCNTypeEditText.setText(identification.getGTCNtype());
        GTCNNumberEditText.setText(identification.getGTCNnumber());
        fullNameEditText.setText(identification.getFullname());
        dayOfBirthEditText.setText(identification.getDayOfBirth());
        genderEditText.setText(identification.getGender());
        nativePlaceEditText.setText(identification.getNativePlace());
        datedEditText.setText(identification.getDated());
        placeEditText.setText(identification.getPlace());
        if(identification.getFrontImage() != null && identification.getBackSideImage() != null) {
            File file1 = new File(identification.getFrontImage());
            File file2 = new File(identification.getBackSideImage());
            if (file1.exists() && file2.exists()) {
                Bitmap myBitmap1 = null;
                Bitmap myBitmap2 = null;
                try {
                    myBitmap1 = handleSamplingAndRotationBitmap(getApplicationContext(), Uri.fromFile(file1));
                    myBitmap2 = handleSamplingAndRotationBitmap(getApplicationContext(), Uri.fromFile(file2));


                } catch (IOException e) {
                    e.printStackTrace();
                }
                frontImageView.setImageBitmap(myBitmap1);
                backSideImageView.setImageBitmap(myBitmap2);
            }
        }
    }

    void getTextEditText(){
        String GTCNtype = GTCNTypeEditText.getText().toString();
        identification.setGTCNtype(GTCNtype);
        String GTCNnumber = GTCNNumberEditText.getText().toString();
        identification.setGTCNnumber(GTCNnumber);
        String fullname = fullNameEditText.getText().toString();
        identification.setFullname(fullname);
        String dayOfBirth = dayOfBirthEditText.getText().toString();
        identification.setDayOfBirth(dayOfBirth);
        String gender = genderEditText.getText().toString();
        identification.setGender(gender);
        String nativePlace = nativePlaceEditText.getText().toString();
        identification.setNativePlace(nativePlace);
        String dated = datedEditText.getText().toString();
        identification.setDated(dated);
        String place = placeEditText.getText().toString();
        identification.setPlace(place);
    }

    void enableEditText(){
        GTCNTypeEditText.setEnabled(true);
        GTCNNumberEditText.setEnabled(true);
        fullNameEditText.setEnabled(true);
        dayOfBirthEditText.setEnabled(true);
        genderEditText.setEnabled(true);
        nativePlaceEditText.setEnabled(true);
        datedEditText.setEnabled(true);
        placeEditText.setEnabled(true);
    }

    void disableEditText(){
        GTCNTypeEditText.setEnabled(false);
        GTCNNumberEditText.setEnabled(false);
        fullNameEditText.setEnabled(false);
        dayOfBirthEditText.setEnabled(false);
        genderEditText.setEnabled(false);
        nativePlaceEditText.setEnabled(false);
        datedEditText.setEnabled(false);
        placeEditText.setEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(detailLayout.getVisibility() == View.VISIBLE) {
                detailLayout.setVisibility(View.GONE);
                listLayout.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class DeleteDialog extends DialogFragment {
        private IdentificationViewModel identificationViewModel;

        DeleteDialog(IdentificationViewModel identificationViewModel){
            this.identificationViewModel = identificationViewModel;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Bạn chắc muốn xoá?")
                    .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            identificationViewModel.delete(identification);
                        }
                    })
                    .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            return builder.create();
        }
    }
}
