<?php

$tables = require "tabledefs.php";

define('DESTINATION_DIR', __DIR__ . "/java");

$tpl = <<<JAVA
package cz.vojtechvondra.ldbill.psp;

public class %sTableDefinition extends TableDefinition
{
    public %sTableDefinition() {
        tableName = "%s";
        colNames = new String[] {
%s
        };
    }
}

JAVA;

foreach ($tables as $table => $cols) {
	$colDefs = array();
	foreach ($cols as $col) {
		$colDefs[] = '            "' . $col . '"';
	}
	$colDefs = implode(",\n", $colDefs);
	$file = sprintf($tpl, ucfirst($table), ucfirst($table), $table, $colDefs);
	
	if (!is_dir(DESTINATION_DIR)) {
		mkdir(DESTINATION_DIR, 0777, true);
	}

	file_put_contents(DESTINATION_DIR . "/" . ucfirst($table) . "TableDefinition.java", $file);
	echo "Generating class for table $table\n";
}