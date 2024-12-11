#include <stdio.h>
#include <stdlib.h>
#include <time.h>

char* loadBoard(char* filename, long* size, int* lineSize) {
    FILE *fptr;
    fptr = fopen(filename, "r");
    fseek(fptr, 0, SEEK_END);
    *size = ftell(fptr);
    rewind(fptr);
    char *data = malloc(sizeof(char)*(*size));
    fread(data, sizeof(char), *size, fptr);
    while(data[*lineSize] != EOF && data[*lineSize] != '\n') {
        (*lineSize)++;
    }
    fclose(fptr);
    return data;
}

char boardElement(int i, int j, int lineSize, char* data) {
    return data[i * lineSize + j + i];
}

void setBoardElement(int i, int j, int lineSize, char* data, char value) {
    data[i * lineSize + j + i] = value;
}

void findGuard(char* data, int linesNumber, int lineSize, int *resI, int *resJ) {
    int i = 0, j = 0;
    while(i != linesNumber) {
        while(j != lineSize) {
            if(boardElement(i, j, lineSize, data) == '^') {
                *resI = i;
                *resJ = j;
                return;
            }
            j++;
        }
        j = 0;
        i++;
    }
}

void move(char* data, int* guardI, int* guardJ, int* direction, int linesNumber, int lineSize) {
    int offsetI, offsetJ;
    switch (*direction) {
    case 0:
        offsetI = -1, offsetJ = 0;
        break;
    case 1:
        offsetI = 0, offsetJ = 1;
        break;
    case 2:
        offsetI = 1, offsetJ = 0;
        break;
    default:
        offsetI = 0, offsetJ = -1;
        break;
    }
    int canMove = 0;
    if(*guardI + offsetI >= 0 && *guardI + offsetI < linesNumber && *guardJ + offsetJ >= 0 && *guardJ + offsetJ < lineSize) {
        if(boardElement(*guardI + offsetI, *guardJ + offsetJ, lineSize, data) == '#') {
            *direction = (*direction + 1) % 4;
            canMove = 1;
        }
    }
    if(canMove == 0) {
        *guardI = *guardI + offsetI;
        *guardJ = *guardJ + offsetJ;
    }
    
}

void processBoard(char* data, int linesNumber, int lineSize) {
    int guardI, guardJ;
    int guardDirection = 0; // 0 - UP, 1 - RIGHT, 2 - DOWN, 3 - LEFT;
    findGuard(data, linesNumber, lineSize, &guardI, &guardJ);
    while(guardI >= 0 && guardI < linesNumber && guardJ >= 0 && guardJ < lineSize) {
        setBoardElement(guardI, guardJ, lineSize, data, 'X');
        move(data, &guardI, &guardJ, &guardDirection, linesNumber, lineSize);
    }
}

void setDirectionInGrid(char** directionGrid, int i, int j, int lineSize, int direction) {
    directionGrid[i * lineSize + j][direction] = 'X';
}

char getDirectionInGrid(char** directionGrid, int i, int j, int lineSize, int direction) {
    return directionGrid[i * lineSize + j][direction];
}

int processBoardWithDirections(char* data, char** directionsGrid, int linesNumber, int lineSize, int guardIInitial, int guardJInitial) {
    int guardI = guardIInitial, guardJ = guardJInitial;
    int guardDirection = 0; // 0 - UP, 1 - RIGHT, 2 - DOWN, 3 - LEFT;
    while(guardI >= 0 && guardI < linesNumber && guardJ >= 0 && guardJ < lineSize) {
        if(getDirectionInGrid(directionsGrid, guardI, guardJ, lineSize, guardDirection) == 'X') {
            return 0;
        }
        setBoardElement(guardI, guardJ, lineSize, data, 'X');
        setDirectionInGrid(directionsGrid, guardI, guardJ, lineSize, guardDirection);
        move(data, &guardI, &guardJ, &guardDirection, linesNumber, lineSize);
    }
    return 1;
}

int countVisitedPositions(char* data, int linesNumber, int lineSize) {
    int counter = 0;
    for(int i = 0; i < linesNumber; i++) {
        for(int j = 0; j < lineSize; j++) {
            if(boardElement(i, j, lineSize, data) == 'X') {
                counter++;
            }
        }
    }
    return counter;
}

char** createDirectionGrid(int linesNumber, int lineSize) {
    char** result = malloc(sizeof(char*)*lineSize*linesNumber);
    for(int i = 0; i < linesNumber * lineSize; i++) {
        result[i] = malloc(sizeof(char)*4);
        for(int j = 0; j < 4; j++) {
            result[i][j] = 0;
        }
    }
    return result;
}

void freeDirectionsGrid(char** directionsGrid, int linesNumber, int lineSize) {
    for(int i = 0; i < linesNumber * lineSize; i++) {
        free(directionsGrid[i]);
    }
    free(directionsGrid);
}

int countPossibleInfiniteObstacles(char* data, int linesNumber, int lineSize) {
    int counter = 0;
    int guardI, guardJ;
    findGuard(data, linesNumber, lineSize, &guardI, &guardJ);
    for(int i = 0; i < linesNumber; i++) {
        for(int j = 0; j < lineSize; j++) {
            char** directionsGrid = createDirectionGrid(linesNumber, lineSize);
            setBoardElement(guardI, guardJ, lineSize, data, '^');
            int obstacleElement = 1;
            if(boardElement(i, j, lineSize, data) == '#') {
                obstacleElement = 0;
            }
            setBoardElement(i, j, lineSize, data, '#');
            int isGuardLocked = processBoardWithDirections(data, directionsGrid, linesNumber, lineSize, guardI, guardJ);
            if(isGuardLocked == 0) {
                counter++;
            }
            freeDirectionsGrid(directionsGrid, linesNumber, lineSize);
            if(obstacleElement == 1){
                setBoardElement(i, j, lineSize, data, '.');
            }
        }
    }
    return counter;
}

int main() {
    clock_t begin = clock();
    long fileSize;
    int lineSize = 0, guardIInitial, guardJInitial;
    char* data = loadBoard("input.txt", &fileSize, &lineSize);
    int linesNumber = (fileSize + 1) / (lineSize + 1);
    findGuard(data, linesNumber, lineSize, &guardIInitial, &guardJInitial);
    processBoard(data, linesNumber, lineSize);
    printf("visited places: %d\n", countVisitedPositions(data, linesNumber, lineSize));
    setBoardElement(guardIInitial, guardJInitial, lineSize, data, '^');
    printf("number of obstacles blocking the guard: %d\n", countPossibleInfiniteObstacles(data, linesNumber, lineSize));
    free(data);
    clock_t end = clock();
    double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    printf("seconds %f\n", time_spent);
    return 0;
}