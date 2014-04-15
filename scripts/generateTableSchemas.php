<?php

$tables = require "tabledefs.php";

if ($argc > 1) {
	if (is_dir($argv[1])) {
		define('DESTINATION_DIR', $argv[1]);
	} else {
		echo "Directory for generating sources does not exist: {$argv[1]}\n";
		exit(1);
	}
} else {
	define('DESTINATION_DIR', __DIR__ . "/java");
}

$tpl = <<<JAVA
package cz.vojtechvondra.ldbill.psp.tables;

import cz.vojtechvondra.ldbill.psp.TableDefinition;

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
	$file = sprintf($tpl, ucfirst($table), ucfirst($table), getTableName($table), $colDefs);
	
	if (!is_dir(DESTINATION_DIR)) {
		mkdir(DESTINATION_DIR, 0777, true);
	}

	file_put_contents(DESTINATION_DIR . "/" . ucfirst($table) . "TableDefinition.java", $file);
	echo "Generating class for table $table\n";
}

function getTableName($table) {
	if (preg_match("/hl[0-9]{4}s/", $table)) {
		return "hl_hlasovani";
	} elseif (preg_match("/hl[0-9]{4}h[0-9]?/", $table)) {
		return "hl_poslanec";
	}
	return $table;
}