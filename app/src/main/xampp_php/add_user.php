<?php

include("connectBD.php");

$response=array();

if(isset($_GET["nom"]) && isset($_GET["prenom"]) && isset($_GET["email"]) && isset($_GET["num"])){

    $nom=$_GET["nom"];
    $prenom=$_GET["prenom"];
    $email=$_GET["email"];
    $num=$_GET["num"];

    $req=mysqli_query($connection,"insert into utilisateur(nom, prenom, num, email) values('$nom','$prenom','$num','$email')");
    
    if($req){
        $response["succes"]=1;
        $response["msg"]="inserted";

        echo json_encode($response);
    }
    else{
        $response["succes"]=0;
        $response["msg"]="request error";

        echo json_encode($response);
    }
    
}
else{
    $response["succes"]=0;
    $response["msg"]="required field is missing";

    echo json_encode($response);
}

?>