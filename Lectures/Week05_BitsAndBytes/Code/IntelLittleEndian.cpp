#include <cstdint>
#include <bitset>
#include <iostream>

int main(int args, char ** argv){
	int64_t signedInt = 1;
	std::cout << signedInt << std::endl;

	char* ptr = (char*) &signedInt;
	for( int i = 0; i < 8; ++i ){
		char c = ptr[i];
		std::bitset<8> b(c);
		std::cout << b << std::endl;
	}
}
