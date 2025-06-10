package com.example.salledesport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.salledesport.api.ProfileService;
import com.example.salledesport.api.RetrofitClient;
import com.example.salledesport.model.Goal;
import com.example.salledesport.model.GoalUpdateRequest;
import com.example.salledesport.model.UserUpdateRequest;
import com.example.salledesport.model.Utilisateur;
import com.example.salledesport.utils.BaseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.Manifest;

public class EditProfileActivity extends BaseActivity {
    private ShapeableImageView profileImageView;
    private TextInputEditText prenomEditText;
    private TextInputEditText nomEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText telephoneEditText;
    private TextInputEditText tailleEditText;
    private TextInputEditText poidsEditText;
    private TextView changeProfilePicture;
    private ChipGroup genderChipGroup;
    private Chip chipMale, chipFemale, chipOther;
    private ChipGroup objectifChipGroup;
    private Chip chipGain, chipPerte, chipMaintien;
    private MaterialCardView backButton;
    private MaterialButton EnregistrerBtn;
    private Button SupprimerCompteBtn;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private String currentPhotoPath;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            setupSystemBarsBehavior();
            return insets;
        });
        hideSystemUI();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() == null || result.getData().getData() == null) {
                            if (currentPhotoPath != null) {
                                File file = new File(currentPhotoPath);
                                this.selectedImageUri = Uri.fromFile(file);
                                profileImageView.setImageURI(this.selectedImageUri);
                            }
                        } else {
                            this.selectedImageUri = result.getData().getData();
                            profileImageView.setImageURI(this.selectedImageUri);
                        }
                    }
                }
        );

        hideSystemUI();
        initialized();
        loadInformationsUser();

        EnregistrerBtn.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                uploadProfilePicture(selectedImageUri, this::updateInformationsUser);
            } else {
                updateInformationsUser();
            }
        });

        SupprimerCompteBtn.setOnClickListener(v -> showDeleteConfirmationDialog());

        backButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        profileImageView.setOnClickListener(v -> updateProfilePicture());
    }

    private void initialized() {
        prenomEditText = findViewById(R.id.edit_first_name);
        nomEditText = findViewById(R.id.edit_last_name);
        emailEditText = findViewById(R.id.edit_email);
        telephoneEditText = findViewById(R.id.edit_telephone);
        tailleEditText = findViewById(R.id.edit_height);
        poidsEditText = findViewById(R.id.edit_weight);
        profileImageView = findViewById(R.id.edit_profile_image);
        genderChipGroup = findViewById(R.id.gender_chip_group);
        chipMale = findViewById(R.id.chip_male);
        chipFemale = findViewById(R.id.chip_female);
        chipOther = findViewById(R.id.chip_other);
        chipGain = findViewById(R.id.chip_muscle_gain);
        chipPerte = findViewById(R.id.chip_weight_loss);
        chipMaintien = findViewById(R.id.chip_maintenance);
        objectifChipGroup = findViewById(R.id.goal_chip_group);
        EnregistrerBtn = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);
        SupprimerCompteBtn = findViewById(R.id.delete_account_button);
        changeProfilePicture = findViewById(R.id.change_photo);
    }

    private void loadInformationsUser() {
        ProfileService profileService = RetrofitClient.getClient(getApplicationContext()).create(ProfileService.class);
        Call<Utilisateur> call = profileService.getProfile();

        call.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String userJson = getSharedPreferences("auth", MODE_PRIVATE).getString("user", null);
                    if (userJson != null) {
                        Utilisateur user = new Gson().fromJson(userJson, Utilisateur.class);
                        displayInformationsUser(user);
                        Toast.makeText(EditProfileActivity.this,
                                "Données profil chargées avec succès",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this,
                        "Données NON chargées",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateInformationsUser() {
        String userJson = getSharedPreferences("auth", MODE_PRIVATE).getString("user", null);
        if (userJson == null) {
            Toast.makeText(this, "Erreur : utilisateur non trouvé", Toast.LENGTH_SHORT).show();
            return;
        }

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFirst_name(prenomEditText.getText().toString());
        updateRequest.setLast_name(nomEditText.getText().toString());
        updateRequest.setPhone_number(telephoneEditText.getText().toString());
        updateRequest.setEmail(emailEditText.getText().toString());

        try {
            updateRequest.setTaille(Double.parseDouble(tailleEditText.getText().toString()));
            updateRequest.setPoids(Double.parseDouble(poidsEditText.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer des valeurs valides pour la taille et le poids", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedGenderId = genderChipGroup.getCheckedChipId();
        if (selectedGenderId == R.id.chip_male) {
            updateRequest.setGender("Homme");
        } else if (selectedGenderId == R.id.chip_female) {
            updateRequest.setGender("Femme");
        } else if (selectedGenderId == R.id.chip_other) {
            updateRequest.setGender("Autre");
        } else {
            Toast.makeText(this, "Veuillez sélectionner un genre", Toast.LENGTH_SHORT).show();
            return;
        }

        String newGoal = null;
        int selectedGoalId = objectifChipGroup.getCheckedChipId();
        if (selectedGoalId == R.id.chip_muscle_gain) {
            newGoal = "Prise de masse";
        } else if (selectedGoalId == R.id.chip_weight_loss) {
            newGoal = "Perte de poids";
        } else if (selectedGoalId == R.id.chip_maintenance) {
            newGoal = "Maintien";
        } else {
            Toast.makeText(this, "Veuillez sélectionner un objectif", Toast.LENGTH_SHORT).show();
            return;
        }

        ProfileService profileService = RetrofitClient.getClient(getApplicationContext()).create(ProfileService.class);
        Call<Utilisateur> userCall = profileService.updateProfile(updateRequest);
        Call<Goal> goalCall = profileService.changeGoal(new GoalUpdateRequest(newGoal));

        userCall.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SharedPreferences.Editor editor = getSharedPreferences("auth", MODE_PRIVATE).edit();
                    editor.putString("user", new Gson().toJson(response.body()));
                    editor.apply();

                    goalCall.enqueue(new Callback<Goal>() {
                        @Override
                        public void onResponse(Call<Goal> call, Response<Goal> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Profil et objectif mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Erreur lors de la mise à jour de l'objectif", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Goal> call, Throwable t) {
                            Toast.makeText(EditProfileActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EditProfileActivity.this, "Erreur lors de la mise à jour du profil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean displayInformationsUser(Utilisateur user) {
        prenomEditText.setText(user.getFirst_name());
        nomEditText.setText(user.getLast_name());
        emailEditText.setText(user.getEmail());
        telephoneEditText.setText(user.getPhone_number());
        tailleEditText.setText(String.valueOf(user.getTaille()));
        poidsEditText.setText(String.valueOf(user.getPoids()));

        if (user.getGender() != null) {
            switch (user.getGender().toLowerCase()) {
                case "homme":
                    chipMale.setChecked(true);
                    break;
                case "femme":
                    chipFemale.setChecked(true);
                    break;
                case "autre":
                    chipOther.setChecked(true);
                    break;
            }
        }

        if (user.getGoals() != null && !user.getGoals().isEmpty()) {
            for (Goal goal : user.getGoals()) {
                if (goal != null && goal.getName() != null) {
                    switch (goal.getName()) {
                        case "Perte de poids":
                            chipPerte.setChecked(true);
                            break;
                        case "Prise de masse":
                            chipGain.setChecked(true);
                            break;
                        case "Maintien":
                            chipMaintien.setChecked(true);
                            break;
                    }
                }
            }
        }

        if (user.getProfile_picture() != null && !user.getProfile_picture().isEmpty()) {
            String image = RetrofitClient.getBaseUrl() + user.getProfile_picture();
            Glide.with(this)
                    .load(image)
                    .error(R.drawable.ic_edit_profile)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            Log.e("GLIDE", "Erreur de chargement", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(profileImageView);
        }

        return true;
    }

    public void updateProfilePicture() {
        String userJson = getSharedPreferences("auth", MODE_PRIVATE).getString("user", null);
        if (userJson == null) {
            Toast.makeText(this, "Erreur : utilisateur non trouvé", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkAndRequestPermissions()) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Choisir une option")
                .setItems(new CharSequence[]{"Prendre une photo", "Choisir depuis la galerie"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            dispatchTakePictureIntent();
                            break;
                        case 1:
                            chooseFromGallery();
                            break;
                    }
                })
                .show();
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void dispatchTakePictureIntent() {
        Log.d("CAMERA", "Dispatching take picture intent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Erreur lors de la création du fichier", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.salledesport.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                imagePickerLauncher.launch(takePictureIntent);
            }
        }
    }

    private MultipartBody.Part prepareImagePart(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            inputStream.close();

            MediaType mediaType = MediaType.parse("image/*");
            RequestBody requestFile = RequestBody.create(mediaType, imageBytes);
            return MultipartBody.Part.createFormData("profile_picture", "image.jpg", requestFile);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la lecture de l'image", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void uploadProfilePicture(Uri imageUri, Runnable onSuccess) {
        MultipartBody.Part imagePart = prepareImagePart(imageUri);
        if (imagePart == null) return;

        ProfileService profileService = RetrofitClient.getClient(getApplicationContext()).create(ProfileService.class);
        Call<Utilisateur> call = profileService.updateProfilePicture(imagePart);

        call.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Photo mise à jour avec succès", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences("auth", MODE_PRIVATE).edit();
                    editor.putString("user", new Gson().toJson(response.body()));
                    editor.apply();
                    if (onSuccess != null) onSuccess.run();
                } else {
                    Toast.makeText(getApplicationContext(), "Erreur lors de la mise à jour de la photo : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Échec de la mise à jour de la photo : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void SupprimerCompte() {
        String userJson = getSharedPreferences("auth", MODE_PRIVATE).getString("user", null);
        if (userJson == null) {
            Toast.makeText(this, "Erreur : utilisateur non trouvé", Toast.LENGTH_SHORT).show();
            return;
        }
        ProfileService profileService = RetrofitClient.getClient(getApplicationContext()).create(ProfileService.class);
        Call<Utilisateur> call = profileService.deleteProfile();

        call.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = getSharedPreferences("auth", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    Toast.makeText(EditProfileActivity.this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Échec de la suppression du compte", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirmation")
                .setContentText("Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible.")
                .setConfirmText("Oui")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();
                    SupprimerCompte();
                })
                .setCancelText("Annuler")
                .setCancelClickListener(SweetAlertDialog::dismiss)
                .show();
    }

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CODE);
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }
}