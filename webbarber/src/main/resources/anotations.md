Entidade Barbeiro

Possíveis soluções: 
1. Cada barbeiro terá uma instância do db.
2. Agrupar todos os dados em nas mesmas tabelas, e quando for
pegar os horários disponíveis ou marcar, etc... filtrar pelo
id do barbeiro.


`Caminhos e parâmetros da URL`

Talvez seja mais interessante passar ids e outros atributos como
request param, não como path variable, ou com o json.

Exemplo: ao atualizar um serviço, não passar o serviceId pela
URL, mas por um body ou de alguma outra forma.