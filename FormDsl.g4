grammar FormDsl;

form: 'Form' ID '{' fields '}';
fields: field (',' field)*;
field: ID;

ID: [a-zA-Z_][a-zA-Z_0-9]*;
WS: [ \t\r\n]+ -> skip;
