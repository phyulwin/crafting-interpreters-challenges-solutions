static uint8_t fieldConstant(Token* className, Token* field) {
	int len = className->length + 1 + field->length;
	char* buf = ALLOCATE(char, len + 1);
	memcpy(buf, className->start, className->length);
	buf[className->length] = '$';
	memcpy(buf + className->length + 1, field->start, field->length);
	buf[len] = '\0';
	return makeConstant(OBJ_VAL(takeString(buf, len)));
}

static void dot(bool canAssign) {
	bool wasThis = thisAccess;
	thisAccess = false;

	consume(TOKEN_IDENTIFIER, "Expect property name after '.'.");
	Token field = parser.previous;

	bool mangle = wasThis && currentClass != NULL;

	if (canAssign && match(TOKEN_EQUAL)) {
		expression();
		uint8_t name = mangle ? fieldConstant(&currentClass->name, &field)
													: makeConstant(OBJ_VAL(copyString(field.start, field.length)));
		emitBytes(OP_SET_PROPERTY, name);
	} else if (match(TOKEN_LEFT_PAREN)) {
		uint8_t plainName = makeConstant(OBJ_VAL(copyString(field.start, field.length)));
		uint8_t argCount = argumentList();
		emitBytes(OP_INVOKE, plainName);
		emitByte(argCount);
	} else {
		uint8_t name = mangle ? fieldConstant(&currentClass->name, &field)
													: makeConstant(OBJ_VAL(copyString(field.start, field.length)));
		emitBytes(OP_GET_PROPERTY, name);
	}
}