import zmq
from random import choice
import time
import threading
import msgpack

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
    events = [b'EV00245', b'EV00246', b'EV00247', b'EV00248', b'EV00249']
    params = [[123,1,2,3,4], [123,1,1,1], [123,1,2,1], [123,1,3,2]]
    while True:
        param = choice(params)
        msg = choice(events) + msgpack.packb(param) 
        print 'PUB ->' ,msg, str(param)
        socket.send( msg )
        time.sleep(interval)

tPub = threading.Thread(target=pub, args=(1,))
tPub.daemon = True
tPub.start()

while True:
    time.sleep(1)
    #msg=msgpack.packb([1, 2, 3])
    #print msg
    #print msgpack.packb(['sql', 'SELECT * FROM LOG LIMIT 1000'])