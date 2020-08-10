# <p align="center">Cache Manager<br/>Client - Server Cache Manager Service App</p>

This is a distributed cache manager service to support multiple readers/writers. Think of a service to manage in-memory cache which can support such clients. Although the design is distribued we did not consider high availability for this exercise. 

Guidelines for the design
----
> 1. Readers/Writers are random and can make requests at any time
> 2. Strong consistency guarantees: A reader should never read stale data (data written to already)
> 3. Readers should get notified if the data they have has gone stale
> 4. Readers get preference over writers, multiple readers-one writer at a time
> 5. No starvation


This Project has 2 Components :-<br />
---------
**1. CacheManager**<br />
Its a Client app which can be installed on any Android device (API 19 to 22). It uses services of CacheServer to share a Global data file stored on Server.


**2. CacheServer**<br />
Its a backend server responsible for managing the Global Cache and giving access of Lock to the devices based on FCFS.


Credits
---------
This application uses Open Source components. You can find the source code and References below. I acknowledge and grateful for their contributions to open source.

References :-<br />
http://tutorials.jenkov.com/java-concurrency/locks.html<br />
https://developers.google.com/cloud-messaging/<br />


Developer
---------
Ramanpreet Singh Khinda [<img src="https://github.com/ramanpreetSinghKhinda/CSE_535_Multilingual_Search_System/blob/master/Resources/linkedin.png" height="50" width="50">](https://www.linkedin.com/in/ramanpreetSinghKhinda)

License
----------
Copyright {2016} 
{Ramanpreet Singh Khinda rkhinda@buffalo.edu} 

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
