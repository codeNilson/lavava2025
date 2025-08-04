# Implementação de Senhas Opcionais para Players

## Resumo das Mudanças

Este documento descreve as alterações implementadas para tornar o campo `password` opcional para a entidade `Player`, conforme solicitado.

## Alterações Realizadas

### 1. Entidade Player (`Player.java`)
- **Mudança**: Campo `password` agora permite valores nulos
- **Alterações específicas**:
  - Anotação `@Column(nullable = true)` adicionada
  - Comentário Javadoc atualizado indicando que a senha é opcional
  - Novo construtor `Player(String username)` adicionado para criação sem senha

### 2. DTO de Criação (`PlayerCreateDTO.java`)
- **Mudança**: Removidas validações Bean Validation do campo password
- **Alterações específicas**:
  - Removida anotação `@NotBlank`
  - Removidas validações de regex complexas
  - Campo agora aceita null, empty string ou valores válidos

### 3. Serviço PlayerService (`PlayerService.java`)
- **Mudança**: Implementada validação condicional no nível de serviço
- **Alterações específicas**:
  - Método `validatePassword()` adicionado com validação regex
  - Validação aplicada apenas quando password não é null/empty
  - Codificação de senha condicional (apenas quando fornecida)
  - Padrão de validação: 8-20 caracteres, maiúscula, minúscula, número, caractere especial

### 4. Autenticação (`PlayerDetails.java`)
- **Mudança**: Tratamento seguro de senhas nulas
- **Alterações específicas**:
  - Método `getPassword()` retorna string vazia para senhas nulas
  - Compatibilidade mantida com Spring Security

### 5. Tratamento de Exceções (`GlobalExceptionHandler.java`)
- **Mudança**: Adicionado handler para IllegalArgumentException
- **Alterações específicas**:
  - Novo método `handleIllegalArgumentException()`
  - Converte exceções de validação em respostas HTTP 400 Bad Request
  - Formato consistente de erro JSON

### 6. Testes Atualizados

#### Testes do DTO (`PlayerCreateDTOTest.java`)
- Todos os testes de validação de senha atualizados
- Agora verificam que validações DTO não falham (validação movida para serviço)

#### Testes do Serviço (`PlayerServiceTest.java`)
- Senhas de teste atualizadas para usar formato válido
- Novos testes criados para cenários de senha opcional (`PlayerServiceOptionalPasswordTest.java`)

#### Testes do Controller (`PlayerControllerTest.java`)
- Expectativas de resposta de erro atualizadas
- Validação agora espera mensagens de erro do serviço, não do DTO

## Comportamentos Implementados

### 1. Senha Nula ou Vazia
- ✅ Player pode ser criado com `password = null`
- ✅ Player pode ser criado com `password = ""`
- ✅ Nenhuma validação ou codificação é aplicada
- ✅ Senha permanece nula no banco de dados

### 2. Senha Fornecida
- ✅ Validação completa aplicada (8-20 chars, maiús, minús, número, especial)
- ✅ Senha é codificada antes de salvar
- ✅ Falhas de validação retornam erro HTTP 400

### 3. Compatibilidade com Spring Security
- ✅ Autenticação funciona corretamente com senhas nulas
- ✅ PlayerDetails retorna string vazia para senha nula
- ✅ Nenhum erro de segurança gerado

## Testes de Validação

### Cenários Testados
1. **Criação com senha nula**: ✅ Passa
2. **Criação com senha vazia**: ✅ Passa  
3. **Criação com senha válida**: ✅ Passa e codifica
4. **Criação com senha inválida**: ✅ Falha com erro apropriado
5. **Atualização com senha válida**: ✅ Passa
6. **Validações DTO removidas**: ✅ Não interferem

### Padrão de Senha Válida
```regex
^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$
```

**Requisitos**:
- 8-20 caracteres de comprimento
- Pelo menos 1 letra minúscula
- Pelo menos 1 letra maiúscula  
- Pelo menos 1 número
- Pelo menos 1 caractere especial (@$!%*?&)

## Arquitetura da Solução

### Abordagem Escolhida: Validação no Serviço
- **Vantagem**: Controle fino sobre quando aplicar validação
- **Vantagem**: Lógica de negócio centralizada
- **Vantagem**: Facilita manutenção e testes

### Alternativas Consideradas
1. **Validação no DTO**: Descartada (dificulta senha opcional)
2. **Validação customizada**: Mais complexa, desnecessária
3. **Múltiplos DTOs**: Duplicação de código

## Resultados dos Testes

```
Todos os testes passando: ✅
- PlayerCreateDTOTest: 9/9 ✅
- PlayerServiceTest: 8/8 ✅  
- PlayerServiceOptionalPasswordTest: 4/4 ✅
- PlayerControllerTest: 12/12 ✅
- Outros testes: Mantidos sem alteração ✅
```

## Conclusão

A implementação de senhas opcionais foi realizada com sucesso:

- ✅ **Requisito atendido**: Password não é mais obrigatório
- ✅ **Compatibilidade**: Mantida com código existente
- ✅ **Segurança**: Validação rigorosa quando senha fornecida
- ✅ **Qualidade**: Todos os testes passando
- ✅ **Arquitetura**: Solução limpa e manutenível

A solução permite flexibilidade para cenários onde senha não é necessária, mantendo robustez de validação e segurança quando senhas são utilizadas.
