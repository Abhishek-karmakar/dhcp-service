## DHCP service

1. Design	a	dynamic	IP	address	allocation	service	with	the	following	requirements	in	mind:	
	a. The	service	must	expose	two	endpoints:	
		1. One	for	IP	address	allocation	with		
			interface AllocationService {
			String allocate(String macAddress);
			}
		2. Second	for	the	heartbeat	message	to	renew	the	allocation		
			interface HeartbeatService {
			Boolean refresh(String macAddress,
			String allocatedIPAddr);
			}
b.IP	address	range	must	be	configurable
c.Expiry	time	must	be	configurable	
d.Each	client	must	be	allocated	a	unique	IP	address
e.IPs	must	be	“leased”,	rather	permanently	allocated,	which	means	the	allocation	
should	expire	after	certain	time.	This	implies	that	the	client	must	regularly	hit	the	
heartbeat	endpoint	to	update	its	presence	
f. How	well	will	the	service	scale?	