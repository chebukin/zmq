import zmq
from random import choice
import time
import threading

host="tcp://127.0.0.1:"

print 'Starting server...'
 
def rep(interval):
    portPub=host+"5500"
    print "Starting REP on port "+portPub
    context = zmq.Context()
    socket = context.socket(zmq.REP)
    socket.bind(portPub)
    while True:
        msg = socket.recv()
        print "<--", msg
        socket.send(msg)

tRep = threading.Thread(target=rep, args=(1,))
tRep.daemon = True
tRep.start()
time.sleep(1)

def pub(interval):
    portPub=host+"5000"
    print "Starting PUB on port "+portPub
    context = zmq.Context()
    socket = context.socket(zmq.PUB)
    socket.bind(portPub)
    events = ['EV00245', 'EV00246', 'EV00247', 'EV00248', 'EV00249']
    params = ['123','124','125','126']
    while True:
        msg = choice(events) + " " + choice(params) 
        print 'PUB ->' ,msg
        socket.send( msg )
        time.sleep(interval)

tPub = threading.Thread(target=pub, args=(1,))
tPub.daemon = True
tPub.start()

while True:
    time.sleep(1)