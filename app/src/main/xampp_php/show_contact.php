
<?php

include("connectBD.php");

$response = array();

if(isset($_GET["nom"])&&isset($_GET["num"])){

    $nom=$_GET["nom"];
    $num=$_GET["num"];

    $req = mysqli_query($connection,"SELECT u.nom, u.prenom, u.email
                        FROM contact c
                        JOIN utilisateur u ON c.id_contact = u.id_nom
                        JOIN utilisateur u2 ON c.id_nom = u2.id_nom
                        WHERE u2.nom = '$nom'
                        AND u2.num = '$num';");

    if (mysqli_num_rows($req) > 0) {

        $info = array();
        $response["contacts"] = array();

        while ($row = mysqli_fetch_array($req)) {
        
            $info["nom"] = $row["nom"];
            $info["prenom"] = $row["prenom"];
            $info["email"] = $row["email"];

            array_push($response["contacts"], $info);
        }
        
        $response["succes"] = 1;
        echo json_encode($response);

    } else {
        
        $response["succes"] = 2;
        $response["message"] = "No contact found";
        echo json_encode($response);
    }
}
else{
    $response["succes"]=0;
    $response["msg"]="required field is missing";
    echo json_encode($response);
}
?>