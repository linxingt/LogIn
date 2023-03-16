<?php

include("connectBD.php");

$response=array();

if(isset($_POST["nom"])&&isset($_POST["num"])){

    $nom=$_POST["nom"];
    $num=$_POST["num"];

    $req=mysqli_query($connection,"select * from utilisateur where nom='$nom' and num='$num'");
    if(mysqli_num_rows($req)>0){

        $response["succes"]=1;
        $response["msg"]="login done successfully";
        echo json_encode($response);

    }
    else{
        $response["succes"]=0;
        $response["msg"]="nom or mdp wrong";
        echo json_encode($response);

    }
}
else{
    $response["succes"]=0;
    $response["msg"]="required field is missing";
    echo json_encode($response);
}

?>