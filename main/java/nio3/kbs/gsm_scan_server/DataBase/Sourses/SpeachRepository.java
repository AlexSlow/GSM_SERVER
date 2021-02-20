package nio3.kbs.gsm_scan_server.DataBase.Sourses;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SpeachRepository {
  Optional<Speach> findById(Long id);
  List<Speach> getPage(Page p);
  List <Speach> getAllByPeriod(Date dateStart, Date dateEnd);
  List <Speach> getByPeriod(Page p,Date dateStart, Date dateEnd);
  List <Speach> findAll();
  List<Speach> getPageOrderById(Page p);
  Long getCountofRows();
  //Получить все записи (Постранично), у которых id больше чем id
  List<Speach> findAllByIdMore(Page p,Long id);
  //Получить последний id для мониторинга
  Long findLastId ();
}
