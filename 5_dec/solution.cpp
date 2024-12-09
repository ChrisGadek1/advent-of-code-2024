#include <stdio.h>
#include <iostream>
#include <fstream>
#include <map>

using namespace std;

std::unordered_map<int, std::unordered_map<int, bool> > getRules(vector<string> data) {
    std::unordered_map<int, std::unordered_map<int, bool> > currentMap;
    
    for(int i = 0; i < data.size(); i++){
        size_t pos = data[i].find("|");
        int after = std::stoi(data[i].substr(pos + 1));
        int before = std::stoi(data[i].substr(0, pos));
        if(currentMap.find(before) == currentMap.end()) {
            std::unordered_map<int, bool> innerMap;
            innerMap[after] = true;
            currentMap[before] = innerMap;
        }
        else {
            currentMap[before][after] = true;
        }
    }

    return currentMap;
}

vector<int> parsePage(string page, char delimeter) {
    int begin = 0, end = 1;
    vector<int> partialResult;
    while(end < page.size() + 1) {
        if(end == page.size() || page[end] == delimeter) {
            partialResult.push_back(std::stoi(page.substr(begin, end - begin)));
            begin = end + 1;
            end = begin + 1;
        }
        else {
            end++;
        }
    }
    return partialResult;
}

vector<vector<int> > getPages(vector<string> data) {
    vector<vector<int> > result;
    for(int i = 0; i < data.size(); i++) {
        result.push_back(parsePage(data[i], ','));
    }
    return result;
}

vector<string> parseFile(string path) {
    ifstream ReadFileStream(path);
    string line;
    vector<string> result;

    while (getline (ReadFileStream, line)) {
        result.push_back(line);
    }
    return result;
}

vector<string> getRulesLines(vector<string> lines) {
    vector<string> result;
    for(int i = 0; i < lines.size(); i++) {
        if(lines[i].size() == 0) {
            break;
        }
        result.push_back(lines[i]);
    }
    return result;
}

vector<string> getPagesLines(vector<string> lines) {
    vector<string> result;
    bool canRead = false;
    for(int i = 0; i < lines.size(); i++) {
        if(lines[i].size() == 0) {
            canRead = true;
        }
        else if(canRead) {
            result.push_back(lines[i]);
        }
    }
    return result;
}

bool isPageCorrect(vector<int> page, std::unordered_map<int, std::unordered_map<int, bool> > rules) {
    for(int i = 0; i < page.size(); i++) {
        for(int j = i - 1; j >= 0; j--) {
            if(rules[page[i]][page[j]]) {
                return false;
            }
        }
    }
    return true;
}

void fixIncorrectPageInIndex(vector<int>& page, std::unordered_map<int, std::unordered_map<int, bool> >& rules, int currentValueInPage) {
        for(int j = currentValueInPage - 1; j >= 0; j--) {
            if(rules[page[currentValueInPage]][page[j]]) {
                int tmpValue = page[currentValueInPage];
                page[currentValueInPage] = page[j];
                page[j] = tmpValue;
                fixIncorrectPageInIndex(page, rules, j++);
            }
        }
}

void fixWholeIncorrectPage(vector<int>& page, std::unordered_map<int, std::unordered_map<int, bool> >& rules) {
    for(int i = page.size() - 1; i >= 0; i--) {
        fixIncorrectPageInIndex(page, rules, i);
    }
}

int getMiddleValueFromPage(vector<int> page) {
    return page[page.size() / 2];
}


int main () {
    string path = "input.txt";
    vector<string> parsedFile = parseFile(path);

    vector<string> parsedPagesLines = getPagesLines(parsedFile);
    vector<vector<int> > pages = getPages(parsedPagesLines);

    vector<string> parsedRulesLines = getRulesLines(parsedFile);
    std::unordered_map<int, std::unordered_map<int, bool> > rules = getRules(parsedRulesLines);
    int resultNotFixed = 0;
    int resultFixed = 0;
    for(int i = 0; i < pages.size(); i++) {
        if(isPageCorrect(pages[i], rules)) {
            resultNotFixed += getMiddleValueFromPage(pages[i]);
        }
        else {
            fixWholeIncorrectPage(pages[i], rules);
            resultFixed += getMiddleValueFromPage(pages[i]);
        }
    }
    cout << "sum from not touched updates: " << resultNotFixed << endl;
    cout << "sum from fixed updates: " << resultFixed << endl;
}
