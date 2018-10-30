.386
CUBE 	macro	NUM
		mov ax, word ptr NUM
		imul ax, word ptr NUM
		imul ax, word ptr NUM
		endm

CUBENUM macro	NUM
		local GODIV
		mov ax, NUM
		xor si, si
		mov bl, 10
GODIV:	div bl
		mov dl, ah ;get one of the numbre
		mov dh, 0
		mov ah, 0
		mov di, dx
		imul dx, di
		imul dx, di
		add si, dx
		cmp ax, 0
		jnbe GODIV
		mov ax, si
		endm

ISTHENUM	macro	MAX, ENDPATH ;para1  the max number ,para2   the ending location 
		local S1, NEQUAL
		pusha
		xor bp, bp
		mov cx, MAX
S1:		nop
		CUBENUM cx
		cmp ax, cx
		jne NEQUAL ;if not water flower number , go loop
		mov word ptr ENDPATH[bp], ax ;write the num to memory
		add bp, 2
NEQUAL:	dec cx
		cmp cx, 0
		ja S1
		mov word ptr ENDPATH[bp], -1 ; put the end tag
		popa
		endm

RETURN 	macro
		mov ax, 004ch
		int 21h
		endm

PRINT	macro	S
		push dx
		lea dx, S
		mov ah, 9
		int 21h
		pop dx
		endm

INPUT	macro	LOCATION
		LOCAL ASC2NUM
		pusha
		lea dx, INTMPE[0]
		mov ah, 10 ;call interupt to input number
		int 21h
		mov cx, word ptr INTMPE[0] ;set cx as the input count
		mov cl, ch
		mov ch, 0
		xor bp, bp
		xor bx, bx
		mov bp, 2
ASC2NUM:mov al, byte ptr INTMPE[bp] ;read next num to ax
		mov ah, 0
		sub ax, 48 ;get the real value of number
		imul bx, 10
		add bx, ax
		inc bp
		loop ASC2NUM  ;loop to read all num
		mov word ptr LOCATION, bx ; write the result from register to memory
		popa
		endm

TOASCII	macro	INPUTSTRING, OUTPUTSTRING
		local FINISH, GODIV, STA2MEM, GETNEXT
		pusha
		mov al, 10
		mov cl, al ;init cl as 10, prapare for div
		xor si, si
		xor bp, bp
GETNEXT:xor dx, dx
		mov ax, word ptr INPUTSTRING[bp]
		xor cx, cx ; init cx = 0, prepare for loop
		cmp ax, -1; if at the end of nums, go finish
		je FINISH

GODIV:	mov bl, 10
		div bl; al = quotient ah = remnder
		mov dl, ah
		add dl, 48 ;transfor to ascii
		push dx ;store
		inc cx ; loop num ++
		mov ah, 0
		cmp ax, 0 ;if(ax != 0) continue to div
		jne GODIV ;if not 0, continue div
		
STA2MEM:pop dx
		mov byte ptr OUTPUTSTRING[si], dl
		inc si
		loop STA2MEM; write the ascii to memory
		mov byte ptr OUTPUTSTRING[si], 0dh
		inc si
		mov byte ptr OUTPUTSTRING[si], 0ah
		inc si
		add bp, 2 ; inc the index of input

		jmp GETNEXT; go to handle the next number

FINISH:	inc si
		mov OUTPUTSTRING[si], '$'
		popa
		endm

LOOPTODETERMIN	macro START, MAX ; para1  the path of data, para2   the max
		local NEXTNUM, FINISHLOOP
		pusha
		xor bp, bp
		xor bx, bx
NEXTNUM:mov ax, word ptr START[bp] ;get the number of group
		cmp ax, MAX ; compare with the max
		jnb FINISHLOOP ;if num > max, go finish
		add bp, 2 ;else inc count and continue loop
		jmp NEXTNUM
FINISHLOOP:
		mov word ptr START[bp], -1 ; put the end tag
		popa
		endm

DATA	segment	use16

TABLE	dw 1, 153, 370, 371, 407, -1
RESULT	dw 10 dup(0)
OUTPUT	db 50 dup(0), '$'
AREA	dw 0
WAY		dw 0
INTMPE	db 5, 5 dup(0)
LETIN	db 0dh, 0ah, 'Please input a number', 0dh, 0ah, '$'
INERRS	db 0dh, 0ah, 0dh, 0ah, 'Please input a number below 1000', '$'
CHOOSE	db 0dh, 0ah, 'Please choose the mode of data sorce.', 0dh, 0ah
		db '1.table', 0dh, 0ah
		db '2.calculate', 0dh, 0ah, '$'
INERRN	db 0dh, 0ah, 0dh, 0ah, 'Please input a number between 0 and 3', 0dh, 0ah, '$'
OUTTIP	db 0dh, 0ah, 'The water flower number you want are:', 0dh, 0ah, '$'

DATA	ends

STACK	segment	use16
		db 100 dup(0)
STACK	ends

CODE	segment use16
		assume ds:DATA,ss:STACK,cs:CODE

INERR:	PRINT INERRS ; print the err message
		jmp AIN ;input again

WAYERR:	PRINT INERRN ; the choose mode err handle
		jmp NIN

START:	mov ax, DATA
		mov ds, ax
		mov ax, STACK
		mov ss, ax
AIN:	PRINT LETIN
		INPUT AREA  ;input the bound of water flower number
		mov ax, AREA  ;read the number to register
		cmp ax, 1000
		jnbe INERR ;if num > 1000, jmp err

NIN:	PRINT CHOOSE ;let user choose the mode of number source
		INPUT WAY
		mov ax, WAY
		cmp ax, 2
		jnbe WAYERR ;if way > 2, err
		cmp ax, 0
		jbe WAYERR ; if way < 0, err
		cmp ax, 1
		jne CALCU ; if way != 1 , go to calculate way
		LOOPTODETERMIN TABLE, AREA
		TOASCII TABLE, OUTPUT
		jmp ENDPROC

CALCU:	ISTHENUM AREA, RESULT
		TOASCII RESULT, OUTPUT

ENDPROC:PRINT OUTTIP
		PRINT OUTPUT
		RETURN

CODE	ends
end START