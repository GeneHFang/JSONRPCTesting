#include <jsonrpccpp/server.h>
#include <jsonrpccpp/server/connectors/httpserver.h>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>

#include "calculateserverstub.h"

using namespace jsonrpc;
using namespace std;

class CalculateServer : public calculateserverstub {
public:
  CalculateServer(AbstractServerConnector &connector, int port);

  virtual void notifyServer();
  virtual std::string serviceInfo();
  virtual double plus(double param1, double param2);
  
private:
  int portNum;
};

CalculateServer::CalculateServer(AbstractServerConnector &connector,
                                 int port) :
  calculateserverstub(connector){
  portNum = port;
  cout << "server up and listening on port " << port << endl;
}

void CalculateServer::notifyServer(){
  cout << "Calculate server notified" << endl;
}


double CalculateServer::plus(double param1, double param2) {
   cout << "Requested " << param1 << " + " << param2 << " returning "
        << (param1 + param2) << endl;
   return param1 + param2;
}


int main(int argc, char * argv[]) {
   int port = 8080;
   if(argc > 1){
      port = atoi(argv[1]);
   }
   //cout << port << endl;
   HttpServer httpserver(port);
   CalculateServer cs(httpserver, port);
   cs.StartListening();
   int c = getchar();
   cs.StopListening();
   return 0;
}
