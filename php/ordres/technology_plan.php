<?php
session_start();
error_reporting(E_ALL & ~E_DEPRECATED);
ini_set('display_errors', 1);
define('USE_PDO', true);

include "../mysql_compat.php";
include "../secure/config.php";
include "../secure/connect.txt";
include "../script/aut.txt";
include "../script/fonctions.txt";

$commandant = intval($commandant);
if (!$commandant || $commandant === 0) {
    die('non connecté');
}

// on récupère les données
$stmt = $pdo->query("
SELECT a.NUMERO, a.RACE, a.NOM,
       t.BENEFICIAIRE as donne_a , t.TECHNOLOGIE as donne_technologie,
       connues.CODE connue_codes, connues.PARAM as connue_param, 
       cherchables.CODE cherchables_codes, cherchables.PARAM as cherchables_param
FROM aa_registre a
    JOIN z_technologies_connues connues ON (a.NUMERO = connues.NUMERO) 
    JOIN z_technologies_cherchees cherchables ON (a.NUMERO = cherchables.NUMERO)
    LEFT JOIN transferer_technologie t ON (a.NUMERO = t.NUMERO)
WHERE a.NUMERO IN (
    SELECT NUMERO FROM _share_technology WHERE SHARE_WITH = $commandant
    UNION
    SELECT $commandant
)
ORDER BY a.NUMERO ASC
");

$commandants = [];
$technologies = [];
while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    $connues = explode(',', $row['connue_codes']);
    $cherchables = explode(',', $row['cherchables_codes']);
    $connuesNames = explode(',', $row['connue_param']);
    $cherchablesNames = explode(',', $row['cherchables_param']);

    $commandants[] = array(
            'numero' => $row['NUMERO'],
            'race' => $row['RACE'],
            'nom' => $row['NOM'],
            'connue_codes' => $connues,
            'cherchables_codes' => $cherchables,
            'donne_a' => $row['donne_a'],
            'donne_technologie' => $row['donne_technologie']
    );

    foreach ($connues as $i => $code) {
        $technologies[$code] = ucfirst($connuesNames[$i]);
    }
    foreach ($cherchables as $i => $code) {
        $technologies[$code] = ucfirst($cherchablesNames[$i]);
    }
}

asort($technologies, CASE_LOWER);

// traitement des données
if (array_key_exists('action', $_POST)) {
    $sql = "DELETE FROM transferer_technologie WHERE NUMERO IN (".implode(',', array_column($commandants, 'numero')).");\n";
    foreach ($commandants as $commandant) {
        $current = intval($commandant['numero']);
        $keys = array_filter($_POST, function ($key) use ($current) {
           return explode('_', $key)[0] == $current;
        }, ARRAY_FILTER_USE_KEY);
        foreach ($keys as $key=>$code) {
            $data = explode('_', $key);
            $beneficiaireNumero = intval($data[1]);
            $beneficiaire = null;
            foreach ($commandants as $c) {
                if($c['numero'] == $beneficiaireNumero){
                    $beneficiaire = $c;
                }
            }
            if($code !== '' && $beneficiaire){
                $sql .= "insert into transferer_technologie (NUMERO, BENEFICIAIRE, TECHNOLOGIE, MODE) 
                        values ($current, {$beneficiaire['numero']}, \"$code\", 1);\n";
                break;
            }
        }
    }
    $res = $pdo->exec($sql);
    header("Location: technology_plan.php");
    exit();
}


$table = "ok";
include_once './body.txt';

function comm($commandant)
{
    return display_author($commandant['nom'], $commandant['numero'], $commandant['race']);
}

?>
<form method="post">
    <input type="hidden" name="action"/>
    <table class="stylized">
        <thead>
        <th></th>
        <?php foreach ($commandants as $commandant) : ?>
            <th><?= comm($commandant) ?></th>
        <?php endforeach; ?>
        </thead>
        <tbody>
        <?php for ($j = 0; $j < count($commandants); $j++) { ?>
            <tr>
                <th align="right">
                    <?= comm($commandants[$j]) ?> donne à
                </th>

                <?php for ($d = 0; $d < count($commandants); $d++) { ?>
                    <?php
                    if ($commandants[$j]['numero'] === $commandants[$d]['numero']) {
                        echo "<td style='background-color: #000'></td>";
                    } else { ?>
                        <td>
                            <select style="width: 100%;"
                                    name="<?= $commandants[$j]['numero'] ?>_<?= $commandants[$d]['numero'] ?>">
                                <option value="">-</option>
                                <?php
                                $technologiesDonnableCodesList = array_intersect(
                                        $commandants[$j]['connue_codes'],
                                        $commandants[$d]['cherchables_codes']
                                );
                                $technologiesDonnableList = [];
                                foreach ($technologiesDonnableCodesList as $code) {
                                    $technologiesDonnableList[$code] = $technologies[$code];
                                }
                                asort($technologiesDonnableList, CASE_LOWER);
                                $beneficiaire = $commandants[$j]['donne_a'] === $commandants[$d]['numero'];
                                $selected = $beneficiaire ? $commandants[$j]['donne_technologie'] : '';
                                foreach ($technologiesDonnableList as $code=>$name) : ?>
                                    <option value="<?= $code ?>" <?= $selected === $code ? 'selected' : '' ?>><?= $name ?></option>
                                <?php endforeach; ?>
                            </select>
                        </td>
                    <?php } ?>
                <?php } ?>
            </tr>
        <?php } ?>
        </tbody>
    </table>
    <input type="submit" value="Enregistrer"/>
</form>