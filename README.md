# PsicoApp (Android-Java)
It is a useful tool for psychologists who need to keep track of patients and their exams in an online database. In addition, the app allows user authentication and has an intuitive user interface that allows users to easily and efficiently add, delete and view patient and exam information.

## Requirements
The application was developed in Android Studio and uses a Firebase NoSQL database. In order to run the application, it is necessary to have a Firebase account and add the corresponding credentials in the google-services.json file.

This Android application allows users to register patients in a Firebase NoSQL database, add images of the patient's exams, and display the corresponding record. The following is a brief description of each of the screens that make up the application:

### HomeActivity.java
The home screen of the application, from here the user can access to different options of the application, for example, he can decide to see the list of patients, register patients or he can delete them from the list.

### Adapter.java
This class is a RecyclerView that serves as a list of patients that will be shown in the FichaPacientes_Activity screen.

### FichaImagenExamenPA_Activity.java
This screen displays the exam images for a particular patient. The user can add new images or delete existing ones.

### FichaPacientes_Activity.java
In this screen, the user can see a list of all the patients registered in the Firebase database. When selecting a patient, the patient's file is displayed with personal data, including images of the associated exams.

### HomeDrawerActivity.java
Class in charge of managing the drop-down menu that appears on the main screen, which is left to the decision of each one to activate it or not.

### ImageAdapter.java
This class is used to adapt the images of the exams to the design of the list of images shown in the FichaImagenExamenPA_Activity screen.

### LoginActivity.java
This screen allows the user to login to the application.

### mostrarActivity.java
Class used to display the list of patients. Patients can be searched using a filter and can also be deleted.

### mostrarFotosPaActivity.java
This screen displays the images of the exams of a particular patient in gallery format.

### NuevoPacienteActivity.java
In this screen, the user can add a new patient to the Firebase database.

### RegisterActivity.java
This screen allows the user to register in the application.

### subirFotosPaActivity.java
This screen allows the user to add new exam images for a particular patient.

## Classes in charge of defining the attributes and methods.

### Usuarios.java
This class defines the structure of the user objects that are stored in Firebase.

### Pacientes.java
This class defines the structure of the patient objects that are stored in Firebase.

### Imagenes.java
This class defines the structure of the image objects that are stored in Firebase.
