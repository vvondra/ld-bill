<?php

$tables = require "tabledefs.php";

$tpl = <<<JAVA
package cz.vojtechvondra.ldbill.psp;

public class %sTableDefinition extends TableDefinition {
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
	file_put_contents(__DIR__ . "/java/" . ucfirst($table) . "TableDefinition.java", $file);
	echo "Generating class for table $table\n";
}