grammar FormDsl;

form: 'Form' ID '{' fields '}' button title;
fields: field (',' field)*;
field: ID ':' type (options)?;
type: 'text' | 'select';
options: '{' option (',' option)* '}';
option: ID;
button: 'Button' '{' 'text' ':' STRING ',' 'color' ':' STRING '}';
title: 'Title' '{' 'color' ':' STRING '}';

ID: [a-zA-Z_][a-zA-Z_0-9]*;
STRING: '"' (~["\\] | '\\' .)* '"';
WS: [ \t\r\n]+ -> skip;
