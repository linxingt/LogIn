<?php

include("connectBD.php");

$response=array();

if(isset($_GET["nom"]) && isset($_GET["num"]) && isset($_GET["cnom"]) && isset($_GET["cprenom"]) && isset($_GET["cemail"])){

    $nom=$_GET["nom"];
    $num=$_GET["num"];
    $cnom=$_GET["cnom"];
    $cprenom=$_GET["cprenom"];
    $cemail=$_GET["cemail"];

    $req1=mysqli_query($connection, "DELETE FROM contact WHERE id_nom = (SELECT id_nom FROM utilisateur WHERE nom = '$nom' AND num = '$num') 
                        AND id_contact = (SELECT id_nom FROM utilisateur WHERE nom = '$cnom'AND prenom = '$cprenom'AND email = '$cemail')");

    $req2=mysqli_query($connection, "DELETE FROM contact WHERE id_nom = (SELECT id_nom FROM utilisateur WHERE nom = '$cnom'AND prenom = '$cprenom'AND email = '$cemail') 
                        AND id_contact = (SELECT id_nom FROM utilisateur WHERE nom = '$nom' AND num = '$num') ");

    
    if($req1 & $req2){
        $response["succes"]=1;
        $response["msg"]="successful delete";

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