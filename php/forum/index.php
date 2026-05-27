<?php require_once '../includes/top.php'; ?>
    <style>
        .forum-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
        .forum-table th, .forum-table td { border: 1px solid #444; padding: 10px; text-align: left; }
        .forum-table th { background-color: #333; color: #fff; }
        .category-header { background-color: #222; color: #fff; padding: 10px; font-size: 1.2em; border: 1px solid #444; }
        .forum-desc { font-size: 0.9em; color: #aaa; }
    </style>
<main style="max-width: 100%">
        <h1>Forum</h1>

        <?php
        require_once 'functions.php';
        $commandant = check_auth();
        global $base;

        $sql_cat = "SELECT * FROM _category ORDER BY id_category ASC";
        $res_cat = mysql($base, $sql_cat);
        ?>
        <?php while ($cat = mysql_fetch_assoc($res_cat)): ?>
            <table class="forum-table">
                <tbody>
                        <tr>
                            <th>Forum <?php echo htmlspecialchars($cat['name']); ?></th>
                            <th>Sujets</th>
                            <th>Dernier message</th>
                        </tr>
                    <?php
                    $sql_forum = "SELECT * FROM _forum WHERE id_category = " . (int)$cat['id_category'];
                    $res_forum = mysql($base, $sql_forum);
                    while ($forum = mysql_fetch_assoc($res_forum)):
                        $id_f = (int)$forum['id_forum'];
                        $sql_count = "SELECT COUNT(*) as nb FROM _post WHERE id_forum = $id_f AND (id_parent IS NULL OR id_parent = 0)";
                        $res_count = mysql($base, $sql_count);
                        $count = mysql_fetch_assoc($res_count);

                        // Dernier message du forum
                        $sql_last = "SELECT p.id_post, p.id_parent, p.record, r.NOM, r.NUMERO, r.RACE 
                                     FROM _post p 
                                     LEFT JOIN aa_registre r ON (r.NUMERO = p.id_author) 
                                     WHERE p.id_forum = $id_f 
                                     ORDER BY p.record DESC LIMIT 1";
                        $res_last = mysql($base, $sql_last);
                        $last = mysql_fetch_assoc($res_last);
                    ?>
                        <tr>
                            <td>
                                <a href="view_forum.php?id=<?php echo $forum['id_forum']; ?>">
                                    <strong><?php echo htmlspecialchars($forum['name']); ?></strong>
                                </a><br>
                                <span class="forum-desc"><?php echo htmlspecialchars($forum['description']); ?></span>
                            </td>
                            <td><?php echo $count['nb']; ?></td>
                            <td>
                                <?php if ($last): 
                                    $target_topic = $last['id_parent'] ? $last['id_parent'] : $last['id_post'];
                                ?>
                                    <a href="view_topic.php?id=<?php echo $target_topic; ?>#post-<?php echo $last['id_post']; ?>" style="text-decoration: none; color: inherit;">
                                        Par <?php echo display_author($last['NOM'], $last['NUMERO'], $last['RACE']); ?><br>
                                        Le <?php echo format_date($last['record']); ?>
                                    </a>
                                <?php else: ?>
                                    Aucun message
                                <?php endif; ?>
                            </td>
                        </tr>
                    <?php endwhile; ?>
                </tbody>
            </table>
        <?php endwhile; ?>
    </div>
</main>
<?php require_once '../includes/bot.php'; ?>
