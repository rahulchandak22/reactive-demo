import { Injectable,NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  constructor(private _zone: NgZone, private http: HttpClient) { }

  getMovies(): Observable<any> {

    return Observable.create(observer => {
      const eventSource = new EventSource("http://localhost:8080");

    eventSource.onmessage = event =>{
      this._zone.run(()=>{
        observer.next(event);
      })
    };

    eventSource.onerror = error => {
      this._zone.run(()=> {
        observer.error(error);
      })
    }
    })
  }

  addMovie(movieName: string): Observable<string>{
    const movieReq = {
      "id": new Date().getTime(),
      "title": movieName
    }
    return this.http.post<string>("http://localhost:8080/movie",movieReq)
  }
}
